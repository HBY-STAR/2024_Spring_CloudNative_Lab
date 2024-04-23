# 2024 Spring CloudNative Lab2

## 1.小组分工

1. 路明畅：mysql、redis
2. 梁宇峰：前四个服务
3. 臧佳俊：后两个服务+ingress
4. 侯斌洋：Helm

ddl: 5.1晚上

## 2. 运行
```shell
minikube start --driver=docker --image-mirror-country=cn --kubernetes-version=v1.28.3 --memory=6144 --cni=flannel
kubectl apply -f k8s_yaml # 报错则多试几次，初次安装ingress-controller需要代理
kubectl get pods -A # 获取所有节点，等待所有节点状态为completed或running
kubectl port-forward svc/ingress-nginx-controller -n ingress-nginx 80:80 # 端口映射，之后浏览器停用缓存且访问http://localhost:80即可运行。

```

## 3.TODO

1. helm chart整合