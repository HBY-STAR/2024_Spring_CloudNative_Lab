# 2024 春 云原生软件技术 lab1

## 小组分工：

侯斌洋：任务3

臧佳俊：任务2-half

梁宇峰：任务1

路明畅：任务2-half

## 其他：

1. ase_contribute-service 在构建运行时使用 jdk17 ，jdk21会报错。
2. windows 使用 docker desktop 需要开启HyperV，而 HyperV 与 VMware Workstation 以及大部分安卓模拟器不兼容。故若使用了这些软件则 HyperV 默认为关闭或隐藏。
3. windows 下可以通过 git bash 运行脚本。
4. 第一次运行中间件以及服务（即那两个脚本）时，尽量挂梯子。
5. 运行中间件时，若仅naco运行失败，可能是本地的mysql占用了3306端口，需要关闭本地mysql进程
6. 已添加IDEA的运行配置到./run下，打开IDEA应该能直接看见配置AseContributeServiceApplication。运行前需要先运行中间件，经测试可以正常运行。