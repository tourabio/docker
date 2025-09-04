# Stage 1: Build the application
FROM maven:3.8.5-openjdk-11 AS builder

# Set working directory
WORKDIR /app

# Copy Maven files for dependency caching
COPY pom.xml .

# Copy the source code
COPY src ./src

# Build the application
RUN mvn clean package -DskipTests

# Stage 2: Create the runtime image
FROM openjdk:11-jre-slim

# Set working directory
WORKDIR /app

# Create a non-root user
RUN useradd -m -u 1000 appuser

# Copy the built JAR from the builder stage
COPY --from=builder /app/target/todo-app-1.0.0.jar ./todo-app.jar

# Change ownership of the application files
RUN chown -R appuser:appuser /app

# Switch to non-root user
USER appuser

# Expose port 8080 (common for Spring Boot applications, assuming TodoApplication.java uses this)
EXPOSE 8080

# Health check to verify the application is running
HEALTHCHECK --interval=30s --timeout=3s --start-period=5s --retries=3 \
  CMD curl -f http://localhost:8080/actuator/health || exit 1

# Run the application
CMD ["java", "-jar", "todo-app.jar"]