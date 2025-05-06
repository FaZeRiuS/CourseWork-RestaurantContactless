FROM ubuntu:latest
LABEL authors="mazur"

ENTRYPOINT ["top", "-b"]

# Use Maven with JDK 24 for building
FROM maven:3.9.9-eclipse-temurin-24 AS build

# Set working directory
WORKDIR /app

# Copy project files
COPY pom.xml .
COPY src ./src

# Build the application
RUN mvn clean package -DskipTests

# Use Eclipse Temurin JDK 24 to run the app
FROM eclipse-temurin:24-jdk

WORKDIR /app

# Copy the JAR file from build stage
COPY --from=build /app/target/*.jar app.jar

# Expose the application port
EXPOSE 8081

# Run the JAR
ENTRYPOINT ["java", "-jar", "app.jar"]
