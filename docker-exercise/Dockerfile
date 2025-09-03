# Docker Exercise: Complete the Dockerfile for the Java Todo Application
#
# Instructions for students:
# 1. Choose an appropriate base image for a Java application
# 2. Set the working directory
# 3. Copy the necessary files
# 4. Build the application
# 5. Expose the correct port
# 6. Define the command to run the application

# TODO 1: Use an appropriate Java base image (hint: Todo Application is using java 11)
FROM maven:3.8.4-openjdk-11-slim AS build

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
# Hint: You might need to:
# - Install Maven if not in base image
# - Run: mvn clean package
# - Or use a multi-stage build approach
# Build with dependency resolution and copy dependencies separately
RUN mvn clean package -DskipTests
RUN mvn dependency:copy-dependencies -DoutputDirectory=target/lib

# Multi-stage build: Use a smaller runtime image
FROM openjdk:11-jre-slim AS runtime

# Install curl for health check (or remove health check if not needed)
RUN apt-get update && apt-get install -y curl && \
    apt-get clean && rm -rf /var/lib/apt/lists/*

# Create a non-root user for better security
RUN groupadd -r todoapp && useradd -r -g todoapp todoapp

# Set the working directory
WORKDIR /app

# Copy both JAR and dependencies from build stage
COPY --from=build /app/target/todo-app-*.jar app.jar
COPY --from=build /app/target/lib/ lib/

# Change ownership to non-root user
RUN chown -R todoapp:todoapp /app

# Switch to non-root user
USER todoapp

# TODO 4: Expose the port that the application runs on (check TodoApplication.java for the port number)
EXPOSE 8080

# Health check
HEALTHCHECK --interval=30s --timeout=3s --start-period=30s --retries=3 \
  CMD curl -f http://localhost:8080/actuator/health || exit 1

# TODO 5: Define the command to run the Java application
# Hint: java -jar target/todo-app-1.0.0.jar
# Or if dependencies are separate: java -cp "target/*:target/lib/*" com.example.todoapp.TodoApplication

CMD ["java", "-cp", "app.jar:lib/*", "com.example.todoapp.TodoApplication"]

# BONUS TODO:
# - Add a health check: done
# - Use multi-stage build to reduce image size: done
# - Add a non-root user for better security: done