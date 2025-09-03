FROM maven:3.8.6-openjdk-11 AS build

WORKDIR /app

COPY pom.xml .
RUN mvn dependency:go-offline

COPY src ./src

RUN mvn clean package -DskipTests

# -------------------------
# Runtime stage
# -------------------------
FROM openjdk:11-jre-slim

WORKDIR /app

# Copy app jar
COPY --from=build /app/target/todo-app-1.0.0.jar app.jar
# Copy dependencies
COPY --from=build /app/target/lib ./lib

EXPOSE 8080

# Run with proper classpath
CMD ["java", "-cp", "app.jar:lib/*", "com.example.todoapp.TodoApplication"]
