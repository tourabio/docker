# Use a Maven + JDK 11 base image
FROM maven:3.9.3-eclipse-temurin-11 AS build

# Set working directory inside container
WORKDIR /app

# Copy Maven configuration first for caching dependencies
COPY pom.xml .

# Download dependencies (cached if pom.xml doesn't change)
RUN mvn dependency:go-offline -B

# Copy the source code
COPY src ./src

# Build the application
RUN mvn clean package -DskipTests


# Use a minimal JDK 11 runtime image to reduce image size
FROM eclipse-temurin:11-jre-alpine

# Create a non-root user for security
RUN addgroup -S appgroup && adduser -S appuser -G appgroup
USER appuser

# Set working directory
WORKDIR /app

# Copy the built jar from the build stage
COPY --from=build /app/target/todo-app-1.0.0.jar ./todo-app.jar

# Expose the port the app runs on (assuming 8080, check your TodoApplication.java)
EXPOSE 8080

# Define the command to run the application
CMD ["java", "-jar", "todo-app.jar"]


# Check if port 8080 responds every 30s
HEALTHCHECK --interval=30s --timeout=5s --start-period=10s --retries=3 \
  CMD wget --spider http://localhost:8080/ || exit 1
