FROM openjdk:17-jdk AS build
WORKDIR /app
COPY . /app
RUN ./mvnw clean package -pl ase_contribute_service -am -Dmaven.test.skip=true

FROM openjdk:17-jdk
WORKDIR /app
COPY --from=build /app/ase_contribute_service/target/*.jar /app/app.jar
CMD ["java", "-jar", "app.jar"]