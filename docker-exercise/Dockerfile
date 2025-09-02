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
FROM maven:3.9.6-eclipse-temurin-11 AS builder

# TODO 2: Set the working directory inside the container (e.g., /app)
WORKDIR /app

# Copy Maven files for dependency caching
# (This is done before copying source code for better Docker layer caching)
COPY pom.xml .

# Copy the source code
COPY src ./src

# TODO 3: Install Maven and build the application
# Hint: You might need to:
# - Install Maven if not in base image
# - Run: mvn clean package
# - Or use a multi-stage build approach
RUN mvn -B -DskipTests clean package

# Runtime image (smaller)
FROM eclipse-temurin:11-jre-alpine

# Create non-root user and group
RUN addgroup -S app && adduser -S app -G app

WORKDIR /app

# Copy the built JAR from the builder stage
# This captures the single fat JAR or the main JAR; rename to app.jar
COPY --from=builder /app/target/*.jar /app/app.jar

# TODO 4: Expose the port that the application runs on (check TodoApplication.java for the port number)
EXPOSE 8080

# Optional: install curl for healthcheck
RUN apk add --no-cache curl

# TODO 5: Define the command to run the Java application
# Hint: java -jar target/todo-app-1.0.0.jar
# Or if dependencies are separate: java -cp "target/*:target/lib/*" com.example.todoapp.TodoApplication
CMD ["java", "-jar", "/app/app.jar"]

# BONUS TODO:
# - Add a health check
# - Use multi-stage build to reduce image size
# - Add a non-root user for better security
HEALTHCHECK --interval=30s --timeout=5s --start-period=20s --retries=3 \
  CMD curl -fsS http://localhost:8080/ || exit 1

USER app