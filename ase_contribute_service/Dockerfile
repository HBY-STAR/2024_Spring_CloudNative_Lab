# 构建阶段
FROM openjdk:17-jdk AS builder

# 设置工作目录
WORKDIR /app

# 复制投稿服务的源代码和mvnw文件
COPY ase_contribute_service /app/ase_contribute_service
COPY ase_common /app/ase_common
COPY mvnw /app/mvnw
COPY mvnw.cmd /app/mvnw.cmd
COPY .mvn /app/.mvn
COPY pom.xml /app/pom.xml

# 构建应用程序
RUN ./mvnw clean package -pl ase_contribute_service -am -Dmaven.test.skip=true

# 最终镜像
FROM openjdk:17-jdk

# 设置工作目录
WORKDIR /app

# 从构建阶段复制生成的.jar文件到最终镜像
COPY --from=builder /app/ase_contribute_service/target/*.jar /app/app.jar

# 执行命令
CMD ["java","-jar","app.jar","--spring.profiles.active=prod"]