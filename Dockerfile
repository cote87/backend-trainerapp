# backend/Dockerfile
FROM eclipse-temurin:17-jdk AS build
WORKDIR /app
ARG JAR_FILE=target/backend-trainerapp-1.0.0.jar
COPY ${JAR_FILE} app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","app.jar"]

