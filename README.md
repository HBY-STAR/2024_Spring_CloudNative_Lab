# 2024 Spring CloudNative Lab2

## 1.小组分工

1. 路明畅：mysql、redis
2. 梁宇峰：前四个服务
3. 臧佳俊：后两个服务+ingress
4. 侯斌洋：Helm

ddl: 5.1晚上

## 2. 运行
```shell
minikube start --driver=docker --image-mirror-country=cn --kubernetes-version=v1.28.3 --memory=6144
kubectl apply -f k8s_yaml
```