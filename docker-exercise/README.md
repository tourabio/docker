# Docker Exercise: Java Todo Application

This exercise helps students learn Docker by containerizing a Java-based Todo API application.

## Project Structure

```
docker-exercise/
├── src/
│   └── main/
│       └── java/
│           └── com/
│               └── example/
│                   └── todoapp/
│                       └── TodoApplication.java
├── pom.xml
├── Dockerfile              # Exercise file with TODOs
├── Dockerfile-solution     # Complete solution
└── README.md
```

## Application Overview

The Java Todo API is a simple REST API built with Java's built-in HTTP server and Gson for JSON processing. It provides:

- `GET /` - API information and available endpoints
- `GET /todos` - List all todos
- `POST /todos` - Create a new todo (requires JSON body with "task" field)
- `GET /health` - Health check endpoint

The application runs on port 8080.

## Exercise Instructions

### Step 1: Complete the Dockerfile

Open the `Dockerfile` and complete the TODOs:

1. **Choose a base image**: Select an appropriate Java image
2. **Set working directory**: Define where the app will live in the container
3. **Build the application**: Add commands to build the Java application with Maven
4. **Expose the port**: Specify which port the container will listen on
5. **Run the application**: Define the command to start the Java application

### Step 2: Build the Docker Image

Once you've completed the Dockerfile, build the image:

```bash
docker build -t java-todo-app .
```

### Step 3: Run the Container

Run your containerized application:

```bash
docker run -p 8080:8080 java-todo-app
```

### Step 4: Test the Application

Test the API endpoints:

```bash
# Get API info
curl http://localhost:8080/

# Get all todos
curl http://localhost:8080/todos

# Create a new todo
curl -X POST http://localhost:8080/todos \
  -H "Content-Type: application/json" \
  -d '{"task": "Learn Docker"}'

# Check health
curl http://localhost:8080/health
```

## Learning Objectives

By completing this exercise, students will learn:

1. **Dockerfile basics**: FROM, WORKDIR, COPY, RUN, EXPOSE, CMD
2. **Layer caching**: Why we copy dependency files before source code
3. **Multi-stage builds**: How to create smaller production images (bonus)
4. **Best practices**: Non-root users, health checks, environment variables
5. **Java containerization**: Specific considerations for Java applications

## Hints for Students

- The application uses Java 11, so choose a compatible base image
- Maven is needed to build the application
- Check `TodoApplication.java` to find the port number
- Consider using multi-stage builds to reduce final image size
- The `pom.xml` file defines how to build the application

## Bonus Challenges

1. **Multi-stage build**: Modify your Dockerfile to use multi-stage builds
2. **Non-root user**: Run the application as a non-root user for better security
3. **Health check**: Add a HEALTHCHECK instruction

## Troubleshooting

### Common Issues

1. **Port already in use**: If port 8080 is busy, map to a different port:
   ```bash
   docker run -p 3000:8080 java-todo-app
   ```

2. **Build fails**: Ensure you're in the correct directory and all files are present

3. **Application doesn't start**: Check the logs:
   ```bash
   docker logs <container-id>
   ```

## Comparison with Node.js Example

Compare this Java implementation with the Node.js example in `docker-example-app`:

- **Base images**: Java uses JDK/JRE images vs Node.js images
- **Build process**: Maven vs npm
- **Dependencies**: pom.xml vs package.json
- **Runtime**: JVM vs Node.js runtime
- **Image size**: Java images tend to be larger; multi-stage builds help