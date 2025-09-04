# ===========================
# 1. Build Stage (with Maven)
# ===========================
# TODO 1: Use an appropriate Java base image (Java 11 with Maven for building)
FROM maven:3.8.8-openjdk-11 AS build

# TODO 2: Set the working directory
WORKDIR /app

# Copy only pom.xml first (dependency caching)
COPY pom.xml .

# Download dependencies (improves caching)
RUN mvn dependency:go-offline

# Copy the source code
COPY src ./src

# TODO 3: Build the application
RUN mvn clean package -DskipTests


# ===========================
# 2. Runtime Stage (lighter image)
# ===========================
FROM openjdk:11-jre-slim

# Set working directory
WORKDIR /app

# Copy the built jar from the build stage
COPY --from=build /app/target/todo-app-1.0.0.jar todo-app.jar

# TODO 4: Expose the port that the application runs on
# (assuming it runs on port 8080 — check TodoApplication.java to confirm)
EXPOSE 8080

# TODO 5: Define the command to run the Java application
CMD ["java", "-jar", "todo-app.jar"]

# BONUS TODOs:
# Health check (optional if your app has /actuator/health or /health endpoint)
# HEALTHCHECK --interval=30s --timeout=5s --start-period=10s --retries=3 \
#   CMD curl -f http://localhost:8080/actuator/health || exit 1

# Run as non-root user for better security
RUN adduser --disabled-password --gecos '' appuser && chown -R appuser /app
USER appuser
