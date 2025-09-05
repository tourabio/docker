# ---------------------------
# Stage 1: Build with Maven
# ---------------------------
FROM maven:3.8-openjdk-11-slim AS builder

# Set working directory
WORKDIR /build

# Copy pom.xml and download dependencies (better caching)
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copy the rest of the source code
COPY src ./src

# Build the application (skip tests for faster build)
RUN mvn clean package -DskipTests

# ---------------------------
# Stage 2: Run with JRE
# ---------------------------
FROM openjdk:11-jre-slim

# Create a non-root user for security
RUN useradd -m -u 1000 appuser

# Set working directory
WORKDIR /app

# Copy only the built JAR from builder stage
COPY --from=builder /build/target/todo-app-1.0.0.jar app.jar

# Expose the application port (check your TodoApplication.java, usually 8080)
EXPOSE 8080

# Switch to non-root user
USER appuser

# Command to run the application
CMD ["java", "-jar", "app.jar"]

# Optional: Add a health check (if Actuator is enabled at /actuator/health)
# HEALTHCHECK --interval=30s --timeout=10s --start-period=30s --retries=3 \
#   CMD curl -f http://localhost:8080/actuator/health || exit 1
