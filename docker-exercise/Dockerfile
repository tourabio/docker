# Multi-stage build for optimal image size
FROM maven:3.8-openjdk-11 AS builder

# Set working directory
WORKDIR /app

# Copy Maven files first for better layer caching
COPY pom.xml .
COPY src ./src

# Build the application
RUN mvn clean package

# Final stage with minimal runtime
FROM openjdk:11-jre-slim

# Set working directory
WORKDIR /app

# Copy only the built JAR from previous stage
COPY --from=builder /app/target/todo-app-1.0.0.jar .

# Create non-root user
RUN useradd -r javauser && \
    chown javauser /app

# Switch to non-root user
USER javauser

# Expose application port
EXPOSE 8080

# Add healthcheck
HEALTHCHECK --interval=30s --timeout=3s \
  CMD curl -f http://localhost:8080/health || exit 1

# Run the application
CMD ["java", "-jar", "todo-app-1.0.0.jar"]