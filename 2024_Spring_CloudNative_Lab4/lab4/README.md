# 云原生软件技术 Lab4 服务网格与可观测性实践

> 张皓捷 胡明明 杨特 王浩睿

在本次Lab中，我们将学习使用istio服务网格的特性，并对可观测性进行实践。

**本lab是小组作业**

## 0. 实验用系统概述

Lab4使用的系统和Lab1、2使用的系统基于相同的主题，即论文投稿审稿系统。不过两者在技术和业务层面都有一些不同，如下表所示：

| 项目 | Lab1、Lab2 | Lab4 |
| --- | --- | --- |
| 项目名称 | 论文投稿审稿系统 | 论文投稿审稿系统 |
| 功能完整性 | 没有实现审稿功能 | 实现了审稿和反诉功能，是一个完整的投稿审稿系统 |
| 服务发现 | Lab1使用Nacos, Lab2使用Kubernetes | 使用Kubernetes和Istio |
| 服务调用协议 | Dubbo | Http |
| 分布式事务 | 无 | Seata |
| 服务网关 | Lab1使用Spring Cloud Gateway，Lab2使用Nginx Ingress | Istio Ingress Gateway |

### 0.1 系统新增功能说明

Lab4中的系统实现了完整的投稿审稿功能，包括投稿、审稿、反诉等功能。会议主席开启评审后，每一篇投稿会被分配给三位审稿人进行评审。

系统提供两种稿件分配策略，分别是基于topic相关度的稿件分配和基于审稿平均负担分配，chair在开启审稿的时候需要从中选择一种策略进行稿件分配。

提交审稿信息需要PC member输入三个信息，分别是：稿件评分，评语，稿件的confidence。稿件评分即PC member对分配给自己的稿件进行评分，-2 到 2 分且不能给0分。4个评分分别对应4个状态 ：-2 -> reject， -1 -> weak reject， 1 -> weak accept， 2 -> accept。PC member还需要填写评语，选择稿件的condifence（一种四种：very low ,low , high ,very high）。PC member提交审稿信息后该稿件变为已审稿状态。

Author可以根据评审结果和评语进行rebuttal，rebuttal的目的是针对评审的质疑进行解释。只有没有初次没有录用的论文需要提交rebuttal。一篇论文的3个分数中只要有一个为-1或-2即表示该论文没有录用。author以输入框的形式提交rebuttal信息，rebuttal信息只有一次提交机会。

PC member们收到rebuttal后，可以根据rebuttal的情况可以修改自己的初次评分，也可以不修改，所有的PC Member确认自己的再次评分后，chair发布最终论文录用结果。

### 0.2 技术上的一些考虑

Lab1和Lab2的系统使用了Dubbo作为服务调用协议，而Lab4的系统使用了基于[Spring Http Interface](https://docs.spring.io/spring-framework/reference/integration/rest-clients.html#rest-http-interface)的Http调用。这是因为Istio的[分布式追踪](https://istio.io/latest/zh/docs/tasks/observability/distributed-tracing/overview/)特性是基于Http的，需要应用程序附加一些HTTP头，才能将多个span串联起来。

Lab4的系统使用了Seata作为分布式事务框架，当一个请求涉及到多个服务时，Seata可以保证这些服务的事务一致性。

## 1. 安装Istio与实验用系统

### 1.1 安装Istio

请到Istio的[GitHub主页](https://github.com/istio/istio/releases/tag/1.20.6)上下载Istio。助教出Lab的时候使用的是1.20.6版本，最近的其他几个版本应该也没有什么区别。

下载完成后，请参照[官方文档](https://istio.io/latest/zh/docs/setup/getting-started/)，把Istio安装到你的Kubernetes集群中。如果你还没有Kubernetes集群，可以自己用minikube或k3d搭建一个。

### 1.2 安装实验用系统

助教已经提供所有实验用的中间件及系统的Docker镜像，你可以直接使用这些镜像来部署实验用系统。如果你想自己构建镜像，也可以从源代码构建。

#### 1.2.1 安装中间件

由于我们在Lab2中已经学习过如何在Kubernetes中部署MySQL和Redis，因此我们这次为了方便，不使用Kubernetes，而是和Lab1一样直接使用Docker Compose来部署中间件。

在`lab4/middlewares-docker`下有三个`docker-compose.yml`文件。你可以在局域网内的某个机器或者一个云服务器上将这些中间件启动起来。

#### 1.2.2 安装实验用系统

首先，创建一个名为`ravusage`的namespace。并打开Istio的自动注入功能。设置`istio-injection=enabled`后，Istio会自动在这个namespace下的所有Pod中注入Istio的sidecar，从而实现服务网格的功能。

```bash
kubectl create namespace ravusage
kubectl label namespace ravusage istio-injection=enabled
```

在`lab4/yamls`目录下有一个`ravusage-config.yml`文件。修改其中的`MYSQL_HOST`和`REDIS_HOST`和`SEATA_HOST`为你部署中间件的机器的IP地址。

```yaml
apiVersion: v1
kind: ConfigMap
metadata:
  name: ravusage-config
  namespace: ravusage
data:
  MYSQL_HOST: "192.168.1.121" # 修改这里
  REDIS_HOST: "192.168.1.121" # 修改这里
  SEATA_HOST: "192.168.1.121" # 修改这里
```

修改完成后，直接把`lab4/yamls`目录下的所有yaml文件部署到你的Kubernetes集群中。

```bash
kubectl apply -f lab4/yamls
```

部署完成后，你可以通过`kubectl get pods -n ravusage`查看所有的Pod是否都正常运行。

使用以下命令将Istio的Ingress Gateway暴露到本地端口。

```bash
kubectl port-forward -n istio-system svc/istio-ingressgateway 8080:80
```

然后你可以通过`http://localhost:8080`访问系统。

## 2. 服务网格与可观测性实践

Istio是一个服务网格，其特性主要包括三大块：流量管理、安全和可观测性，具体如下表所述：

| 特性 | 描述 |
| --- | --- |
| 流量管理 | Istio通过其流量管理模型允许操作者控制服务之间的流量和API调用，包括服务发现、负载均衡、熔断器、故障转移、故障注入、请求路由、流量镜像、请求重试等功能。 |
| 安全 | Istio提供了强大的安全特性，包括服务到服务的身份验证、终端用户认证、双向TLS、证书管理以及安全策略的自动应用和强制执行。 |
| 可观测性 | 可观测性主要包含Logging、Metrics、Tracing三个方面。Istio可以与很多可观测性工具集成，如Prometheus、Grafana、Kiali、Skywalking、Jaeger、Loki等，从而对微服务进行监控与分析，帮助运维团队更好地管理微服务系统。 |

本次Lab需要同学们自行参考Istio的[官方文档](https://istio.io/latest/zh/docs/)对Istio服务网格和微服务系统的可观测性进行实践。具体要求如下：

- 自行设计实验场景

- 至少使用一种可观测性工具，对可观测性Logging、Metrics、Tracing三个方面的至少一个进行实践。你可以选择将Istio和可观测性工具集成(官方文档中有较好支持)，也可以独立使用某个可观测性工具。

- 除了可观测性以外，对Istio的其他特性中的至少一个（例如故障注入）进行实践。

- 分析实验结果，说明你的实验对于改进微服务系统或提高微服务系统的可靠性有何帮助。

## 3. 提交内容

1. 实验报告：描述你们小组的实验内容、实验过程及对实验结果的分析，附上能说明实验结果的截图。

2. 相关代码：提交相关的代码

## 4. 截止时间

2024年6月21日（周五） 23:59:59