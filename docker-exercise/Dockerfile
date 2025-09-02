FROM maven:3.9.9-eclipse-temurin-11 AS builder

WORKDIR /app

COPY pom.xml .

COPY src /app/src

RUN mvn clean package


FROM eclipse-temurin:11-jre

COPY --from=builder /app/target/todo-app-1.0.0.jar app.jar

EXPOSE 8080

CMD ["java", "-jar", "app.jar"]

# BONUS TODO: 
# - Add a health check
# - Use multi-stage build to reduce image size
# - Add a non-root user for better security