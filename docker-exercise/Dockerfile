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
FROM eclipse-temurin:11-jdk

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
RUN apt-get update && \
    apt-get install -y maven && \
    mvn clean package

# TODO 4: Expose the port that the application runs on (check TodoApplication.java for the port number)
EXPOSE 8080

# TODO 5: Define the command to run the Java application
# Hint: java -jar target/todo-app-1.0.0.jar
# Or if dependencies are separate: java -cp "target/*:target/lib/*" com.example.todoapp.TodoApplication
CMD ["java", "-jar", "app.jar"]

# BONUS TODO: 
# - Add a health check
# - Use multi-stage build to reduce image size
# - Add a non-root user for better security
HEALTHCHECK --interval=30s --timeout=5s --start-period=10s --retries=3 \
  CMD curl -f http://localhost:8080/actuator/health || exit 1