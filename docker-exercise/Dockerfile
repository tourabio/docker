# ----------------------
# Stage 1: Build the app
# ----------------------
FROM maven:3.8.7-eclipse-temurin-11 AS builder

# TODO 2: Set working directory
WORKDIR /app

# Copy Maven files first (better layer caching)
COPY pom.xml .

# Download dependencies (cached unless pom.xml changes)
RUN mvn dependency:go-offline -B

# Copy the source code
COPY src ./src

# TODO 3: Build the application
RUN mvn clean package -DskipTests

# ----------------------
# Stage 2: Run the app
# ----------------------
FROM eclipse-temurin:11-jre

# Set working directory
WORKDIR /app

# Copy built jar from builder stage
COPY --from=builder /app/target/todo-app-1.0.0.jar app.jar

# TODO 4: Expose port (assuming 8080 from TodoApplication.java)
EXPOSE 8080

# TODO 5: Define command
CMD ["java", "-jar", "app.jar"]

# BONUS: Add a simple healthcheck
HEALTHCHECK --interval=30s --timeout=5s --start-period=10s --retries=3 \
  CMD curl -f http://localhost:8080/actuator/health || exit 1
