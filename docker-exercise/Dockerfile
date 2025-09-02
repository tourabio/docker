# Docker Exercise: Complete the Dockerfile for the Java Todo Application
#
# Instructions for students:
# 1. Choose an appropriate base image for a Java application
# 2. Set the working directory
# 3. Copy the necessary files
# 4. Build the application
# 5. Expose the correct port
# 6. Define the command to run the application

# Multi-stage build for optimal image size
# Stage 1: Build stage
FROM maven:3.8.6-openjdk-11-slim AS build

# TODO 2: Set the working directory inside the container (e.g., /app)
WORKDIR /app

# Copy Maven files for dependency caching
# (This is done before copying source code for better Docker layer caching)
COPY pom.xml .

# Download dependencies (this layer will be cached if pom.xml doesn't change)
RUN mvn dependency:go-offline -B

# Copy the source code
COPY src ./src

# TODO 3: Install Maven and build the application
# Maven is already installed in the base image, so we just need to build
RUN mvn clean package -DskipTests

# Stage 2: Runtime stage
# TODO 1: Use an appropriate Java base image (hint: Todo Application is using java 11)
FROM openjdk:11-jre-slim

# Create a non-root user for better security (BONUS TODO)
RUN groupadd -r todoapp && useradd -r -g todoapp todoapp

# Set the working directory
WORKDIR /app

# Copy the built JAR from the build stage
COPY --from=build /app/target/todo-app-*.jar app.jar

# Change ownership to non-root user
RUN chown -R todoapp:todoapp /app

# Switch to non-root user
USER todoapp

# TODO 4: Expose the port that the application runs on
# Most Spring Boot applications run on port 8080 by default
# Check TodoApplication.java or application.properties for the actual port
EXPOSE 8080

# BONUS TODO: Add a health check
HEALTHCHECK --interval=30s --timeout=3s --start-period=5s --retries=3 \
  CMD curl -f http://localhost:8080/actuator/health || exit 1

# TODO 5: Define the command to run the Java application
CMD ["java", "-jar", "app.jar"]

# Alternative approach if you need to specify classpath:
# CMD ["java", "-cp", "app.jar:lib/*", "com.example.todoapp.TodoApplication"]