# 2024 Spring CloudNative Lab4

## 1. 运行

```shell
# 1. 下载Istio并添加bin目录到path

# 2. 设置docker context
docker context use default

# 3. 运行一次即可，删除之前lab2的集群
minikube delete

# 4. 创建一个新的minikube集群
minikube start --driver=docker --image-mirror-country=cn --kubernetes-version=v1.28.3 --memory=6144

# 5. 安装istio
istioctl install --set profile=demo -y


# 6. 启动中间件
docker compose -f lab4/middlewares-docker/mysql-docker/docker-compose.yaml up -d
docker compose -f lab4/middlewares-docker/redis/docker-compose.yml up -d
docker compose -f lab4/middlewares-docker/seata/docker-compose.yml up -d

# 7. 部署应用
kubectl create namespace ravusage
kubectl label namespace ravusage istio-injection=enabled
# windows使用ipconfig命令查看 Wireless LAN adapter WLAN: 的 Ipv4地址并修改ravusage-config.yml
kubectl apply -f lab4/yamls

# 8. 检查pods是否正常运行，正常情况下所有pod都应该是running状态且不会频繁RESTART
kubectl get pods -n ravusage

# 9. 将Istio的Ingress Gateway暴露到本地端口。
kubectl port-forward -n istio-system svc/istio-ingressgateway 8080:80

# 10. 浏览器停用缓存并访问 http://localhost:8080 查看是否正常运行


# 若仅有review启动失败，则删除之前的volume并重启mysql，然后重启review。因为若有volume存在则mysql不会执行初始化脚本
docker compose -f lab4/middlewares-docker/mysql-docker/docker-compose.yaml down -v
docker compose -f lab4/middlewares-docker/mysql-docker/docker-compose.yaml up -d
kubectl delete pod -n ravusage -l app=ase-review-service

# 下面命令用于debug
# 重启某个pod
kubectl delete pod -n ravusage -l app=ase-user-service
# 查看某个pod的日志
kubectl logs -n ravusage -l app=ase-user-service
```