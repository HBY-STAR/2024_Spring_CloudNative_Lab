# 2024 Spring CloudNative Lab2

## 1.小组分工

1. 路明畅：mysql、redis
2. 梁宇峰：前四个服务
3. 臧佳俊：后两个服务+ingress
4. 侯斌洋：Helm

## 2. 运行

```shell
# 准备 minikube
docker context use default
minikube start --driver=docker --image-mirror-country=cn --kubernetes-version=v1.28.3 --memory=6144 --cni=flannel

# 使用 yaml 文件部署 k8s 集群，包含中间件、服务、ingress。
kubectl apply -f k8s_middlewares  # 部署中间件
kubectl apply -f k8s_services     # 部署服务   
kubectl apply -f k8s_ingress      # 部署ingress
# 或者可以利用 k8s 的 restart 机制，不考虑顺序，使用 kubectl apply -f k8s_all 一键部署。

# 检查节点状态
kubectl get pods -A               
# 端口映射，之后浏览器停用缓存且访问http://localhost:80即可运行。
kubectl port-forward svc/ingress-nginx-controller -n ingress-nginx 80:80  
```

or 

```shell
# 准备 minikube
docker context use default
minikube start --driver=docker --image-mirror-country=cn --kubernetes-version=v1.28.3 --memory=6144 --cni=flannel

# 使用 yaml 文件部署中间件。
kubectl apply -f k8s_middlewares  # 部署中间件

# 使用 Helm Chart 部署服务和ingress。
helm install lab2-chart-0.1.0 ./lab2-chart-0.1.0.tgz

# 检查节点状态
kubectl get pods -A               
# 端口映射，之后浏览器停用缓存且访问http://localhost:80即可运行。
kubectl port-forward svc/ingress-nginx-controller -n ingress-nginx 80:80  
```

## 3.TODO

All Done.

report left