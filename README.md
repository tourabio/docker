# Docker: From Physical Servers to Containers

## Table of Contents
1. [Historical Evolution](#historical-evolution)
2. [What is Docker?](#what-is-docker)
3. [Docker Architecture](#docker-architecture)
4. [Core Docker Concepts](#core-docker-concepts)
5. [Essential Docker Commands](#essential-docker-commands)
6. [Hands-On Exercise](#hands-on-exercise)

## Historical Evolution

### The Journey of Application Deployment

```
┌──────────────┐     ┌──────────────┐     ┌──────────────┐
│   1990s-2000s│────▶│   2000s-2010s│────▶│   2013-Now   │
│   Physical   │     │    Virtual   │     │  Containers  │
│   Servers    │     │   Machines   │     │   (Docker)   │
└──────────────┘     └──────────────┘     └──────────────┘
```

### 1. Physical Servers Era (1990s-2000s)

```
┌─────────────────────────────────┐
│      Physical Server            │
├─────────────────────────────────┤
│         Application             │
├─────────────────────────────────┤
│      Operating System           │
├─────────────────────────────────┤
│         Hardware                │
└─────────────────────────────────┘
```

**Characteristics:**
- **One application per server**: To avoid conflicts
- **Resource underutilization**: Servers often used only 5-15% of capacity
- **High costs**: Hardware, power, cooling, space
- **Slow deployment**: Weeks to procure and setup new servers
- **No isolation**: Application conflicts on shared servers

**Problems:**
- 💰 Expensive hardware costs
- 🐌 Slow scaling
- ⚡ High power consumption
- 🏢 Large data center footprint
- 🔧 Complex maintenance

### 2. Virtual Machines Era (2000s-2010s)

```
┌──────────────────────────────────────────────────┐
│                 Physical Server                  │
├──────────────────────────────────────────────────┤
│                    Hypervisor                    │
├────────────┬────────────┬────────────┬──────────┤
│    VM 1    │    VM 2    │    VM 3    │   VM 4   │
├────────────┼────────────┼────────────┼──────────┤
│   App A    │   App B    │   App C    │  App D   │
├────────────┼────────────┼────────────┼──────────┤
│  Guest OS  │  Guest OS  │  Guest OS  │ Guest OS │
└────────────┴────────────┴────────────┴──────────┘
```

**Improvements:**
- **Better resource utilization**: Multiple VMs per server
- **Isolation**: Each VM is independent
- **Flexibility**: Easy to create, clone, move VMs
- **Snapshot capability**: Save VM states

**Remaining Issues:**
- 🏋️ Heavy resource consumption (each VM needs full OS)
- ⏱️ Slow boot times (minutes)
- 💾 Large disk footprint (GBs per VM)
- 🔄 OS redundancy

### 3. Containers Era (2013-Present)

```
┌──────────────────────────────────────────────────┐
│                 Physical/Virtual Server          │
├──────────────────────────────────────────────────┤
│                 Host Operating System            │
├──────────────────────────────────────────────────┤
│                   Docker Engine                  │
├────────────┬────────────┬────────────┬──────────┤
│ Container1 │ Container2 │ Container3 │Container4│
├────────────┼────────────┼────────────┼──────────┤
│   App A    │   App B    │   App C    │  App D   │
├────────────┼────────────┼────────────┼──────────┤
│   Libs     │   Libs     │   Libs     │   Libs   │
└────────────┴────────────┴────────────┴──────────┘
```

**Revolutionary Advantages:**
- ⚡ **Lightning fast**: Start in milliseconds
- 🪶 **Lightweight**: MBs instead of GBs
- 🚀 **Portable**: "Write once, run anywhere"
- 📦 **Consistent**: Same environment everywhere
- 🔧 **DevOps friendly**: Easy CI/CD integration

## What is Docker?

Docker is a **containerization platform** that packages applications and their dependencies into portable containers. Released in 2013, Docker revolutionized how we build, ship, and run applications.

### Key Innovation
Docker didn't invent containers (Linux containers existed before), but it made them:
- **Easy to use**: Simple commands and workflows
- **Portable**: Containers run identically everywhere
- **Shareable**: Docker Hub for container images

## Docker Architecture

```
┌─────────────────────────────────────────────────┐
│                   Docker Client                 │
│         (docker build, run, pull, push)         │
└─────────────────┬───────────────────────────────┘
                  │ REST API
                  ▼
┌─────────────────────────────────────────────────┐
│                  Docker Daemon                  │
│                   (dockerd)                     │
├─────────────────────────────────────────────────┤
│  ┌──────────┐  ┌──────────┐  ┌──────────────┐ │
│  │  Images  │  │Containers│  │   Networks   │ │
│  └──────────┘  └──────────┘  └──────────────┘ │
│  ┌──────────┐  ┌──────────┐                   │
│  │ Volumes  │  │ Plugins  │                   │
│  └──────────┘  └──────────┘                   │
└─────────────────────────────────────────────────┘
                  │
                  ▼
┌─────────────────────────────────────────────────┐
│              Container Runtime                  │
│               (containerd, runc)                │
└─────────────────────────────────────────────────┘
```

### Architecture Components Explained

#### 1. Docker Client
The **Docker Client** is the primary interface for users to interact with Docker. It's a command-line tool that accepts commands and communicates with the Docker daemon.

**Key Features:**
- **CLI Commands**: Executes commands like `docker run`, `docker build`, `docker pull`
- **API Communication**: Sends requests to Docker daemon via REST API
- **Remote Connection**: Can connect to remote Docker daemons
- **Multiple Clients**: Multiple clients can connect to the same daemon

**How it works:**
```bash
# When you type this command
docker run nginx

# The client:
1. Parses the command
2. Converts it to API request
3. Sends to Docker daemon
4. Displays the response
```

#### 2. Docker Daemon (dockerd)
The **Docker Daemon** is the background service that manages Docker objects. It's the brain of Docker operations.

**Responsibilities:**
- **API Server**: Listens for Docker API requests
- **Object Management**: Creates and manages containers, images, networks, and volumes
- **Registry Communication**: Pulls/pushes images from/to registries
- **Resource Allocation**: Manages system resources for containers

**Configuration:**
```json
// daemon.json example
{
  "debug": true,
  "hosts": ["tcp://0.0.0.0:2375"],
  "storage-driver": "overlay2",
  "log-level": "info"
}
```

#### 3. Docker Objects

##### Images 📦
**Storage location**: `/var/lib/docker/images`
- **Layered filesystem**: Each layer represents a Dockerfile instruction
- **Content-addressable**: Identified by SHA256 hash
- **Shared layers**: Multiple images can share common layers
- **Read-only**: Immutable once created

##### Containers 🏃
**Runtime instances** of Docker images
- **Writable layer**: Adds a thin writable layer on top of image
- **Namespace isolation**: Process, network, mount, IPC, UTS isolation
- **Resource limits**: CPU, memory, I/O constraints via cgroups
- **Lifecycle states**: Created → Running → Paused → Stopped → Removed

##### Networks 🌐
**Communication pathways** between containers
- **Bridge**: Default network, containers on same host
- **Host**: Removes network isolation, uses host networking
- **Overlay**: Multi-host communication in swarm mode
- **Macvlan**: Assigns MAC address, appears as physical device
- **None**: Disables all networking

##### Volumes 💾
**Persistent data storage** mechanism
- **Bind mounts**: Map host directory to container
- **Named volumes**: Docker-managed storage
- **tmpfs mounts**: Temporary filesystem in memory
- **Volume drivers**: Support for external storage systems

##### Plugins 🔌
**Extend Docker functionality**
- **Network plugins**: Custom network drivers
- **Volume plugins**: External storage systems
- **Authorization plugins**: Access control
- **Log plugins**: Custom logging drivers

#### 4. Container Runtime

##### containerd
**High-level container runtime** that manages the complete container lifecycle
- **Image management**: Pull, push, and storage
- **Container execution**: Start, stop, pause, resume
- **Snapshot management**: Filesystem snapshots
- **gRPC API**: Communication interface

##### runc
**Low-level container runtime** that creates and runs containers
- **OCI compliant**: Implements Open Container Initiative spec
- **Lightweight**: Minimal runtime for container execution
- **Namespace creation**: Sets up Linux namespaces
- **Cgroup management**: Configures resource limits

### Communication Flow

```
User Input → Docker Client → REST API → Docker Daemon
                                              ↓
                                        Container Runtime
                                              ↓
                                     Linux Kernel Features
                                    (namespaces, cgroups)
```

**Example: Running a Container**
1. **User**: `docker run -d nginx`
2. **Client**: Sends API request to daemon
3. **Daemon**: 
   - Checks for local nginx image
   - Pulls from registry if needed
   - Creates container configuration
4. **containerd**: 
   - Prepares container bundle
   - Creates snapshots
5. **runc**: 
   - Creates namespaces
   - Sets up cgroups
   - Starts container process
6. **Response**: Container ID returned to user

### Docker API

The Docker Engine API is a RESTful API accessed via HTTP/HTTPS:

**Common Endpoints:**
```bash
# List containers
GET /containers/json

# Create container
POST /containers/create

# Start container
POST /containers/{id}/start

# Get container logs
GET /containers/{id}/logs

# List images
GET /images/json
```

**Example API Call:**
```bash
# Using curl to interact with Docker API
curl --unix-socket /var/run/docker.sock \
  -X GET http://localhost/v1.41/containers/json
```

## Core Docker Concepts

### 1. Docker Images 📦

**What is it?**
A Docker image is a **read-only template** containing application code, runtime, libraries, and dependencies.

**Analogy:** Think of it as a **recipe** or **blueprint** for creating containers.

```
┌─────────────────────┐
│   Docker Image      │
├─────────────────────┤
│  Layer 4: App Code  │ ← Your application
├─────────────────────┤
│  Layer 3: Libraries │ ← Dependencies
├─────────────────────┤
│  Layer 2: Runtime   │ ← Java, Python, Node.js
├─────────────────────┤
│  Layer 1: Base OS   │ ← Ubuntu, Alpine
└─────────────────────┘
```

**Key Features:**
- **Layered structure**: Each instruction creates a new layer
- **Immutable**: Cannot be changed once created
- **Shareable**: Can be pushed/pulled from registries
- **Versioned**: Tagged with versions (e.g., `app:1.0`, `app:latest`)

### 2. Docker Containers 🏃

**What is it?**
A container is a **running instance** of a Docker image.

**Analogy:** If an image is a **recipe**, a container is the **cake** you bake from it.

```
     Docker Image                Docker Containers
    ┌────────────┐              ┌────────────┐
    │            │   docker run  │ Container1 │
    │   Image    │──────────────▶│  (Running) │
    │            │               └────────────┘
    └────────────┘               ┌────────────┐
           │         docker run  │ Container2 │
           └────────────────────▶│  (Running) │
                                └────────────┘
```

**Key Features:**
- **Isolated**: Has its own filesystem, network, process space
- **Ephemeral**: Can be stopped, started, deleted
- **Stateful**: Can maintain state while running
- **Multiple instances**: Many containers from one image

### 3. Dockerfile 📄

**What is it?**
A text file containing instructions to build a Docker image.

**Basic Structure:**
```dockerfile
# Start from a base image
FROM node:18-alpine

# Set working directory
WORKDIR /app

# Copy files
COPY package*.json ./

# Install dependencies
RUN npm install

# Copy application code
COPY . .

# Expose port
EXPOSE 3000

# Run command
CMD ["node", "app.js"]
```

**Common Instructions:**
| Instruction | Purpose | Example |
|------------|---------|---------|
| `FROM` | Base image | `FROM ubuntu:22.04` |
| `RUN` | Execute commands | `RUN apt-get update` |
| `COPY` | Copy files | `COPY . /app` |
| `WORKDIR` | Set working directory | `WORKDIR /app` |
| `ENV` | Set environment variables | `ENV PORT=8080` |
| `EXPOSE` | Document ports | `EXPOSE 8080` |
| `CMD` | Default command | `CMD ["npm", "start"]` |
| `ENTRYPOINT` | Main executable | `ENTRYPOINT ["java", "-jar"]` |

### 4. Docker Registry 🏪

**What is it?**
A storage and distribution system for Docker images.

```
┌──────────────────┐          ┌──────────────────┐
│   Local Machine  │          │  Docker Registry │
│                  │  push    │                  │
│  ┌────────────┐  │─────────▶│  ┌────────────┐ │
│  │   Image    │  │          │  │   Image    │ │
│  └────────────┘  │          │  └────────────┘ │
│                  │  pull    │                  │
│                  │◀─────────│                  │
└──────────────────┘          └──────────────────┘
```

#### Docker Hub
- **Official registry**: hub.docker.com
- **Public & Private repos**: Free tier available
- **Official images**: Verified images (nginx, ubuntu, postgres)
- **Naming convention**: `[registry/]username/repository[:tag]`
  - `nginx` → Official image
  - `myuser/myapp:v1.0` → User image with tag
  - `gcr.io/project/image` → Google Container Registry

**Other Registries:**
- Amazon ECR
- Google Container Registry
- Azure Container Registry
- GitLab Container Registry
- Self-hosted (Harbor, Nexus)

## Essential Docker Commands

### Image Management

```bash
# Pull an image from registry
docker pull nginx:latest

# List local images
docker images

# Build image from Dockerfile
docker build -t myapp:1.0 .

# Tag an image
docker tag myapp:1.0 myuser/myapp:latest

# Push image to registry
docker push myuser/myapp:latest

# Remove an image
docker rmi nginx:latest
```

### Container Management

```bash
# Run a container
docker run -d -p 8080:80 --name webserver nginx

# List running containers
docker ps

# List all containers (including stopped)
docker ps -a

# Stop a container
docker stop webserver

# Start a stopped container
docker start webserver

# Remove a container
docker rm webserver

# Execute command in running container
docker exec -it webserver bash

# View container logs
docker logs webserver

# Inspect container details
docker inspect webserver
```

### Docker Run Options

| Option | Purpose | Example |
|--------|---------|---------|
| `-d` | Run in background (detached) | `docker run -d nginx` |
| `-p` | Port mapping (host:container) | `docker run -p 8080:80 nginx` |
| `--name` | Name the container | `docker run --name web nginx` |
| `-v` | Volume mount | `docker run -v /data:/app/data nginx` |
| `-e` | Environment variable | `docker run -e DB_HOST=localhost nginx` |
| `--rm` | Remove container when stopped | `docker run --rm nginx` |
| `-it` | Interactive terminal | `docker run -it ubuntu bash` |

### System Management

```bash
# Show Docker system info
docker info

# Show Docker version
docker version

# Remove unused data
docker system prune

# Show disk usage
docker system df

# Monitor resource usage
docker stats
```

## Hands-On Exercise

### Exercise: Dockerize a Simple Node.js Todo App

Let's create and containerize a simple Node.js application to understand Docker concepts!

#### Project Structure

```
docker-example-app/
├── app.js              # Main application
├── package.json        # Node dependencies
├── Dockerfile          # Docker build instructions
└── .dockerignore       # Files to exclude
```

#### Step 1: Application Setup

The example app is a simple Todo API with the following endpoints:
- `GET /` - Welcome message
- `GET /todos` - Get all todos
- `POST /todos` - Create a new todo
- `GET /health` - Health check

**File: `docker-example-app/app.js`**
```javascript
const express = require('express');
const app = express();
const PORT = process.env.PORT || 3000;

app.use(express.json());

let todos = [
    { id: 1, task: 'Learn Docker', completed: false },
    { id: 2, task: 'Build an image', completed: false }
];

app.get('/', (req, res) => {
    res.json({ message: 'Welcome to Todo API!' });
});

app.get('/todos', (req, res) => {
    res.json(todos);
});

app.post('/todos', (req, res) => {
    const { task } = req.body;
    const newTodo = { id: todos.length + 1, task, completed: false };
    todos.push(newTodo);
    res.status(201).json(newTodo);
});

app.listen(PORT, () => {
    console.log(`Server running on port ${PORT}`);
});
```

#### Step 2: Understanding the Dockerfile

**File: `docker-example-app/Dockerfile`**
```dockerfile
# 1. Start from a base image
FROM node:18-alpine

# 2. Set working directory
WORKDIR /usr/src/app

# 3. Copy package files first (for better caching)
COPY package*.json ./

# 4. Install dependencies
RUN npm install --production

# 5. Copy application code
COPY . .

# 6. Document the port
EXPOSE 3000

# 7. Define startup command
CMD ["node", "app.js"]
```

**Dockerfile Explained:**
- **Line 1**: Uses Alpine Linux (small, secure base image)
- **Line 2**: Sets `/usr/src/app` as working directory
- **Line 3**: Copies package.json first (dependency caching)
- **Line 4**: Installs only production dependencies
- **Line 5**: Copies all application code
- **Line 6**: Documents that app uses port 3000
- **Line 7**: Runs the application

#### Step 3: Build the Docker Image

```bash
# Navigate to the project directory
cd docker-example-app

# Build the image with a tag
docker build -t todo-app:1.0 .

# Verify the image was created
docker images
```

**What happens during build:**
```
[+] Building 12.3s
 => [1/5] FROM node:18-alpine                    3.2s
 => [2/5] WORKDIR /usr/src/app                   0.1s
 => [3/5] COPY package*.json ./                  0.0s
 => [4/5] RUN npm install --production           8.7s
 => [5/5] COPY . .                               0.1s
 => exporting to image                           0.2s
 => naming to docker.io/library/todo-app:1.0     0.0s
```

#### Step 4: Run the Container

```bash
# Run in detached mode with port mapping
docker run -d -p 3000:3000 --name my-todo-app todo-app:1.0

# Check if container is running
docker ps

# View logs
docker logs my-todo-app
```

#### Step 5: Test the Application

```bash
# Test the welcome endpoint
curl http://localhost:3000/

# Get all todos
curl http://localhost:3000/todos

# Add a new todo
curl -X POST http://localhost:3000/todos \
  -H "Content-Type: application/json" \
  -d '{"task":"Complete Docker tutorial"}'

# Check health
curl http://localhost:3000/health
```

#### Step 6: Container Management

```bash
# Stop the container
docker stop my-todo-app

# Start it again
docker start my-todo-app

# Restart the container
docker restart my-todo-app

# Execute commands inside container
docker exec -it my-todo-app sh
# Inside container shell:
ls -la
node --version
exit

# Remove the container
docker stop my-todo-app
docker rm my-todo-app
```

#### Step 7: Working with Environment Variables

```bash
# Run with custom port
docker run -d -p 4000:4000 -e PORT=4000 --name todo-app-env todo-app:1.0

# Run with NODE_ENV
docker run -d -p 3000:3000 -e NODE_ENV=production --name todo-app-prod todo-app:1.0
```

#### Step 8: Using Docker Volumes

```bash
# Create a volume for persistent data
docker volume create todo-data

# Run with volume
docker run -d -p 3000:3000 \
  -v todo-data:/usr/src/app/data \
  --name todo-app-volume \
  todo-app:1.0

# Inspect the volume
docker volume inspect todo-data
```

#### Step 9: Multi-Stage Build (Advanced)

Create `Dockerfile.multi`:
```dockerfile
# Stage 1: Build stage
FROM node:18-alpine AS builder
WORKDIR /usr/src/app
COPY package*.json ./
RUN npm ci --only=production

# Stage 2: Production stage
FROM node:18-alpine
WORKDIR /usr/src/app
COPY --from=builder /usr/src/app/node_modules ./node_modules
COPY . .
EXPOSE 3000
CMD ["node", "app.js"]
```

Build and compare sizes:
```bash
# Build multi-stage
docker build -f Dockerfile.multi -t todo-app:slim .

# Compare image sizes
docker images | grep todo-app
```

### Practice Tasks

#### 🎯 Basic Tasks
1. ✅ Build the Docker image
2. ✅ Run the container
3. ✅ Test all API endpoints
4. ✅ View container logs
5. ✅ Stop and remove container

#### 🔧 Intermediate Tasks
1. Change the port using environment variables
2. Run multiple instances on different ports
3. Use `docker exec` to explore the container filesystem
4. Create a `.dockerignore` file and rebuild
5. Tag the image with different versions

#### 🚀 Advanced Tasks
1. Push the image to Docker Hub
2. Create a docker-compose.yml file
3. Add a database container (MongoDB or PostgreSQL)
4. Implement health checks in Dockerfile
5. Create development and production Dockerfiles

### Docker Compose Example

Create `docker-compose.yml`:
```yaml
version: '3.8'

services:
  app:
    build: .
    ports:
      - "3000:3000"
    environment:
      - NODE_ENV=development
      - PORT=3000
    volumes:
      - .:/usr/src/app
      - /usr/src/app/node_modules
    command: npm run dev
```

Run with Docker Compose:
```bash
# Start services
docker-compose up

# Run in background
docker-compose up -d

# View logs
docker-compose logs -f

# Stop services
docker-compose down
```

### Troubleshooting Guide

| Problem | Solution |
|---------|----------|
| "Cannot connect to Docker daemon" | Start Docker Desktop or `sudo service docker start` |
| "Port already in use" | Change port or stop conflicting service |
| "No space left on device" | Run `docker system prune -a` |
| Container exits immediately | Check logs: `docker logs container_name` |
| "Permission denied" | Use `sudo` (Linux) or check Docker Desktop |

### Best Practices Checklist

✅ **DO:**
- Use official base images
- Minimize layers in Dockerfile
- Use `.dockerignore` file
- Run as non-root user
- Use multi-stage builds
- Tag images with versions
- Keep images small (Alpine Linux)
- One process per container

❌ **DON'T:**
- Store secrets in images
- Run as root user
- Use `latest` tag in production
- Install unnecessary packages
- Copy `node_modules` folder
- Ignore security updates

## Summary

You've learned:
1. **History**: Evolution from physical servers → VMs → containers
2. **Docker Basics**: Images, containers, Dockerfile, registries
3. **Commands**: Essential Docker CLI commands
4. **Hands-on**: Built and deployed a Node.js application
5. **Best Practices**: Security, optimization, and troubleshooting

### Next Steps
- Learn Docker Compose for multi-container apps
- Explore container orchestration with Kubernetes
- Practice with different application types
- Study Docker networking and volumes in depth
- Implement CI/CD pipelines with Docker

## Resources
- 📚 [Official Docker Docs](https://docs.docker.com)
- 🎮 [Play with Docker](https://labs.play-with-docker.com)
- 🎥 [Docker YouTube Channel](https://www.youtube.com/c/DockerInc)
- 📖 [Docker Hub](https://hub.docker.com)