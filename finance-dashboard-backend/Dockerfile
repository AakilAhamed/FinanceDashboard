# Multi-stage build for Spring Boot application
FROM eclipse-temurin:21-jdk AS builder
WORKDIR /app

COPY .mvn ./.mvn
COPY mvnw ./mvnw
COPY pom.xml .
COPY src ./src

RUN chmod +x mvnw && ./mvnw -DskipTests clean package

FROM eclipse-temurin:21-jre
WORKDIR /app

COPY --from=builder /app/target/*.jar app.jar

ENV DB_HOST=postgres
ENV DB_PORT=5432
ENV DB_NAME=finance_dashboard
ENV DB_USERNAME=postgres
ENV DB_PASSWORD=postgres
ENV JWT_EXPIRATION=86400000
ENV JWT_SECRET=

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
