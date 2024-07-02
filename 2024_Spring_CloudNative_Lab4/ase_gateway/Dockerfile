FROM openjdk:17-jdk as builder
ENV env=prod
# FROM maven:3.8.3-openjdk-17 as builder
# 因为是多模块项目，并且模块之间有相互依赖，因此只能编译父pom.xml
# 编译父pom.xml时，
COPY . /app
WORKDIR /app
# -pl 指定编译的模块(ase_gateway) -am 递归编译指定模块(ase_gateway)的依赖 -Dmaven.test.skip=true 跳过测试
# RUN mvn clean package -pl ase_gateway -am -Dmaven.test.skip=true
RUN ./mvnw clean package -pl ase_gateway -am -Dmaven.test.skip=true

FROM openjdk:17-jdk as runner
COPY --from=builder /app/ase_gateway/target/*.jar /app/app.jar
WORKDIR /app
CMD ["java", "-jar", "app.jar", "--spring.profiles.active=${env}"]