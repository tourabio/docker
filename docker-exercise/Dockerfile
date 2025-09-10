# Docker Exercise: Complete the Dockerfile for the Java Todo Application
#
# Instructions for students:
# 1. Choose an appropriate base image for a Java application
# 2. Set the working directory
# 3. Copy the necessary files
# 4. Build the application
# 5. Expose the correct port
# 6. Define the command to run the application

# Multi-stage build for smaller final image
FROM maven:3.8.6-openjdk-11 AS build

# TODO 2: Set the working directory inside the container (e.g., /app)
WORKDIR /app

# Copy Maven files for dependency caching
# (This is done before copying source code for better Docker layer caching)
COPY pom.xml .

# Download dependencies first (for better caching)
RUN mvn dependency:go-offline -B

# Copy the source code
COPY src ./src

# TODO 3: Install Maven and build the application
# Your pom.xml uses dependency plugin to copy deps to lib/, so use package goal
RUN mvn clean package -DskipTests

# TODO 1: Use an appropriate Java base image for runtime (Java 11)
FROM openjdk:11-jre-slim

# Create a non-root user for better security (BONUS)
RUN groupadd -r todoapp && useradd -r -g todoapp todoapp

# TODO 2: Set the working directory inside the container
WORKDIR /app

# Copy the JAR and the lib directory with dependencies
COPY --from=build /app/target/todo-app-1.0.0.jar app.jar
COPY --from=build /app/target/lib ./lib

# Change ownership to non-root user
RUN chown -R todoapp:todoapp /app
USER todoapp

# TODO 4: Expose the port that the application runs on (port 8080 from TodoApplication.java)
EXPOSE 8080

# BONUS: Add a health check
HEALTHCHECK --interval=30s --timeout=3s --start-period=5s --retries=3 \
  CMD curl -f http://localhost:8080/health || exit 1

# TODO 5: Define the command to run the Java application
CMD ["java", "-jar", "app.jar"]