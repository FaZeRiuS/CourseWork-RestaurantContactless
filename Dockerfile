FROM ubuntu:latest
LABEL authors="mazur"

ENTRYPOINT ["top", "-b"]

FROM maven:3.9.9-eclipse-temurin-24 AS build

WORKDIR /app

COPY pom.xml .
COPY src ./src

RUN mvn clean package -DskipTests

FROM eclipse-temurin:24-jdk

WORKDIR /app

COPY --from=build /app/target/*.jar app.jar

EXPOSE 8081

ENTRYPOINT ["java", "-jar", "app.jar"]
