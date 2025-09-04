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
FROM maven:3.8.5-openjdk-11 AS build

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
RUN mvn dependency:go-offline -B
RUN mvn clean package -DskipTests

# Second stage: Runtime environment
FROM openjdk:11-jre-slim

# Create non-root user for security
RUN groupadd -r todouser && useradd -r -g todouser todouser

# Set working directory
WORKDIR /app

# Copy the built JAR and dependencies from the build stage
COPY --from=build /app/target/todo-app-1.0.0.jar todo-app-1.0.0.jar
COPY --from=build /app/target/lib ./lib

# Change ownership to non-root user
RUN chown -R todouser:todouser /app
USER todouser

# TODO 4: Expose the port that the application runs on
EXPOSE 8080

# TODO 5: Define the command to run the Java application
CMD ["java", "-jar", "todo-app-1.0.0.jar"]