# ---- Build Stage ----
FROM maven:3.9.9-eclipse-temurin-11 AS builder

WORKDIR /app

COPY pom.xml .
RUN mvn dependency:go-offline

COPY src ./src
RUN mvn clean package

# ---- Runtime Stage ----
FROM eclipse-temurin:11-jre

WORKDIR /app

COPY --from=builder /app/target/todo-app-1.0.0.jar app.jar

RUN useradd -m appuser
USER appuser

EXPOSE 8080

CMD ["java", "-jar", "app.jar"]
