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
# FROM ???
FROM maven:3.8.6-eclipse-temurin-11 AS build

# TODO 2: Set the working directory inside the container (e.g., /app)
# WORKDIR ???
WORKDIR /app

# Copy Maven files for dependency caching
# (This is done before copying source code for better Docker layer caching)
COPY pom.xml .
RUN mvn dependency:go-offline

# Copy the source code
COPY src ./src

# TODO 3: Install Maven and build the application
# Hint: You might need to:
# - Install Maven if not in base image
# - Run: mvn clean package
# - Or use a multi-stage build approach
RUN mvn clean package -DskipTests

# Runtime stage with only JRE
FROM eclipse-temurin:11-jre

WORKDIR /app

# Copy the built jar from the build stage
COPY --from=build /app/target/todo-app-1.0.0.jar app.jar

# TODO 4: Expose the port that the application runs on (check TodoApplication.java for the port number)
# EXPOSE ???
EXPOSE 8080

# TODO 5: Define the command to run the Java application
# Hint: java -jar target/todo-app-1.0.0.jar
# Or if dependencies are separate: java -cp "target/*:target/lib/*" com.example.todoapp.TodoApplication
# CMD ???
CMD ["java", "-jar", "app.jar"]

# BONUS TODO:
# - Add a health check
# - Use multi-stage build to reduce image size
# - Add a non-root user for better security

# BONUS TODO: Add a non-root user for better security
RUN useradd -m appuser
USER appuser

# BONUS TODO: Add a health check (assuming your app exposes /health on port 8080)
HEALTHCHECK --interval=30s --timeout=5s --start-period=10s --retries=3 \
  CMD curl -f http://localhost:8080/health || exit 1
