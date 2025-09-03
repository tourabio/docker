# Multi-stage build for smaller final image
# Stage 1: Build stage
FROM maven:3.8-openjdk-11-slim AS builder

# Set working directory for build
WORKDIR /build

# Copy pom.xml first for better layer caching
COPY pom.xml .

# Download dependencies (cached if pom.xml hasn't changed)
RUN mvn dependency:go-offline -B

# Copy source code
COPY src ./src

# Build the application
RUN mvn clean package -DskipTests

# Stage 2: Runtime stage
FROM openjdk:11-jre-slim

# Add a non-root user for security
RUN useradd -m -u 1000 appuser

# Set working directory
WORKDIR /app

# Copy the built JAR and dependencies from builder stage
COPY --from=builder /build/target/todo-app-1.0.0.jar app.jar
COPY --from=builder /build/target/lib ./lib


# Change ownership to non-root user
RUN chown -R appuser:appuser /app

# Switch to non-root user
USER appuser

# Expose the application port
EXPOSE 8080

# Add health check
HEALTHCHECK --interval=30s --timeout=3s --start-period=5s --retries=3 \
  CMD curl -f http://localhost:8080/health || exit 1

# Set environment variable (can be overridden at runtime)
ENV APP_ENV=production

# Run the application
CMD ["java", "-cp", "app.jar:lib/*", "com.example.todoapp.TodoApplication"]