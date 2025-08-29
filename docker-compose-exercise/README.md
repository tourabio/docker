# Docker Compose Exercise: Full-Stack Todo Application

A comprehensive Docker Compose exercise demonstrating multi-service orchestration with Java backend, MySQL database, and HTML frontend.

## Architecture

```
┌─────────────┐     ┌─────────────┐     ┌──────────────┐
│   Frontend  │────▶│   Backend   │────▶│   Database   │
│    (HTML)   │     │   (Java)    │     │   (MySQL)    │
│  Port: 3000 │     │  Port: 8080 │     │  Port: 3306  │
└─────────────┘     └─────────────┘     └──────────────┘
                                              │
                                         ┌────▼────┐
                                         │phpMyAdmin│
                                         │Port:8081│
                                         └─────────┘
```

## Services

### 1. **Database (MySQL)**
- MySQL 8.0 with persistent storage
- Automatic initialization with sample todos
- Health checks for service readiness
- Environment variables for configuration

### 2. **Backend API (Java + HTTP Server)**
- RESTful API with full CRUD operations
- MySQL integration with JDBC
- CORS enabled for frontend communication
- Health endpoint for monitoring

### 3. **Frontend (Static HTML)**
- Vanilla JavaScript with modern UI
- Real-time CRUD operations
- Responsive design with CSS gradients
- Served by nginx:alpine

### 4. **phpMyAdmin (Database UI)**
- Web-based MySQL administration
- Access at http://localhost:8081
- Pre-configured for easy database access

## Project Structure

```
docker-compose-exercise/
├── docker-compose.yaml          # Exercise file with 3 TODOs for students
├── backend/
│   ├── Dockerfile              # Multi-stage build with security
│   ├── pom.xml                 # Maven dependencies
│   └── src/
│       └── main/
│           └── java/
│               └── com/
│                   └── example/
│                       └── todoapp/
│                           └── TodoApplication.java
├── frontend/
│   └── index.html             # Complete frontend application
├── database/
│   └── init.sql              # Database schema and sample data
└── README.md                 # This file
```

## Prerequisites

- Docker Desktop installed
- Docker Compose (included with Docker Desktop)
- 2GB+ free RAM
- Ports 3000, 8080, 3306, 8081 available

## Student Exercise Instructions

### 🎯 Your Mission: Complete the Docker Compose Configuration

This exercise contains **3 TODOs** for you to complete in the `docker-compose.yaml` file:

#### TODO 1: Configure MySQL Database Service
Complete the database service configuration:
- Use `mysql:8.0` image
- Set container name to `todo-mysql-db`
- Configure environment variables for MySQL setup
- Map port 3306 to host
- Create volumes for data persistence and initialization
- Add health check and network configuration

#### TODO 2: Configure Java Backend Service
Complete the backend service configuration:
- Build from the `./backend` directory
- Set container name to `todo-java-backend`
- Configure database connection environment variables
- Map port 8080 to host
- Set up service dependencies with health check
- Configure proper network access

#### TODO 3: Configure Frontend Service
Complete the frontend service configuration:
- Use `nginx:alpine` image
- Set container name to `todo-frontend`
- Map port 80 to host port 3000
- Mount frontend files to nginx directory
- Configure service dependencies and networking

### 🚀 Getting Started

1. **Navigate to the exercise directory:**
```bash
cd docker-compose-exercise
```

2. **Examine the existing files:**
- Look at the completed `phpMyAdmin` service as an example
- Study the backend Java code in `backend/src/main/java/.../TodoApplication.java`
- Review the frontend HTML in `frontend/index.html`
- Check the database initialization script in `database/init.sql`

3. **Complete the TODOs:**
- Open `docker-compose.yaml`
- Find the three TODO sections
- Use the phpMyAdmin service as a reference for syntax
- Refer to the comments for specific requirements

4. **Test your configuration:**
```bash
# Start all services
docker-compose up -d

# Check service status
docker-compose ps

# View logs if needed
docker-compose logs -f
```

5. **Access the applications:**
- Frontend: http://localhost:3000
- Backend API: http://localhost:8080
- Database UI: http://localhost:8081
  - Server: `database`
  - Username: `todouser`
  - Password: `todopass`
  - Database: `tododb`

### 🧪 Testing Your Solution

Once your services are running, test the complete application:

```bash
# Test backend API directly
curl http://localhost:8080/health
curl http://localhost:8080/todos

# Create a todo via API
curl -X POST http://localhost:8080/todos \
  -H "Content-Type: application/json" \
  -d '{"task": "Complete Docker Compose Exercise"}'

# Delete a todo
curl -X DELETE http://localhost:8080/todos/1
```

The frontend at http://localhost:3000 should allow you to:
- View existing todos
- Add new todos
- Delete todos
- See real-time API status

## Learning Objectives

By completing this exercise, students will master:

### 1. **Docker Compose Fundamentals**
- Service definitions and dependencies
- Environment variable configuration
- Port mapping and network communication
- Volume management for persistence

### 2. **Multi-Service Architecture**
- Database integration patterns
- API-frontend communication
- Service discovery using container names
- Health checks and startup ordering

### 3. **Security and Best Practices**
- Network segmentation (frontend ↔ backend ↔ database)
- Non-root user implementation in containers
- Environment-based configuration
- Data persistence strategies

### 4. **Development Workflow**
- Multi-container application orchestration
- Service scaling and management
- Log aggregation and debugging
- Database administration interfaces

## Network Architecture

The solution implements proper network segmentation:

```yaml
networks:
  backend-network:     # Backend ↔ Database ↔ phpMyAdmin
  frontend-network:    # Frontend ↔ Backend
```

**Security Benefits:**
- Frontend cannot directly access database
- Database is isolated from external networks
- phpMyAdmin has dedicated admin access
- Follows principle of least privilege

## Docker Compose Commands

### Basic Operations
```bash
# Start all services in detached mode
docker-compose up -d

# Stop all services
docker-compose down

# View real-time logs
docker-compose logs -f

# View specific service logs
docker-compose logs -f backend
docker-compose logs -f database

# Restart a specific service
docker-compose restart backend
```

### Development Commands
```bash
# Start with live logs (helpful for debugging)
docker-compose up

# Rebuild images after code changes
docker-compose up --build

# Remove everything including volumes (fresh start)
docker-compose down -v

# Check service status
docker-compose ps
```

### Database Operations
```bash
# Access MySQL directly
docker-compose exec database mysql -u todouser -p tododb

# Reset database with fresh data
docker-compose down -v
docker-compose up -d

# Check database health
docker-compose exec database mysqladmin ping -h localhost
```

## API Endpoints

### Backend API (http://localhost:8080)

| Method | Endpoint | Description | Request Body |
|--------|----------|-------------|--------------|
| GET | `/` | API information | - |
| GET | `/health` | Health check | - |
| GET | `/todos` | Get all todos | - |
| POST | `/todos` | Create new todo | `{"task": "Learn Docker"}` |
| DELETE | `/todos/{id}` | Delete specific todo | - |

### Example API Usage
```bash
# Get API information
curl http://localhost:8080/

# Health check
curl http://localhost:8080/health

# Get all todos
curl http://localhost:8080/todos

# Create a new todo
curl -X POST http://localhost:8080/todos \
  -H "Content-Type: application/json" \
  -d '{"task": "Master Docker Compose", "completed": false}'

# Delete todo with ID 1
curl -X DELETE http://localhost:8080/todos/1
```

## Troubleshooting

### Common Issues and Solutions

#### 1. **Services Won't Start**
```bash
# Check which ports are in use
netstat -tlnp | grep :8080  # Linux/Mac
netstat -ano | findstr :8080  # Windows

# Change ports in docker-compose.yaml if conflicts exist
ports:
  - "8090:8080"  # Use different host port
```

#### 2. **Database Connection Failed**
```bash
# Check database container status
docker-compose logs database

# Verify database is healthy
docker-compose ps

# Test database connectivity
docker-compose exec backend ping database
```

#### 3. **Frontend Can't Connect to Backend**
- Verify backend is running on port 8080
- Check CORS configuration in Java backend
- Ensure both services are on same network
- Test API directly: `curl http://localhost:8080/health`

#### 4. **Build Failures**
```bash
# Clean build with no cache
docker-compose build --no-cache

# Check for syntax errors in docker-compose.yaml
docker-compose config

# Rebuild specific service
docker-compose build backend
```

#### 5. **Permission Issues**
```bash
# On Linux/Mac, ensure Docker daemon is running
sudo systemctl start docker

# Check file permissions for mounted volumes
ls -la frontend/
ls -la backend/
ls -la database/
```

## Advanced Challenges

Once you complete the basic exercise, try these enhancements:

### 1. **Add Resource Limits**
```yaml
services:
  backend:
    deploy:
      resources:
        limits:
          cpus: '0.5'
          memory: 512M
```

### 2. **Implement Restart Policies**
```yaml
services:
  backend:
    restart: unless-stopped
```

### 3. **Add Logging Configuration**
```yaml
services:
  backend:
    logging:
      driver: json-file
      options:
        max-size: "10m"
        max-file: "3"
```

### 4. **Create Development Override**
Create `docker-compose.override.yaml` for development-specific settings:
```yaml
version: '3.8'
services:
  backend:
    volumes:
      - ./backend:/app
    environment:
      - APP_ENV=development
```

## Hints for Success

### 🔍 **Before You Start:**
1. Study the phpMyAdmin service configuration as your template
2. Look at the Java application code to understand database requirements
3. Check the database init.sql to see the expected schema
4. Note the network names already defined at the bottom

### 💡 **While Working:**
1. Use the exact container names specified in the TODOs
2. Environment variables should match what the Java code expects
3. Health check commands should be appropriate for each service type
4. Don't forget the `condition: service_healthy` for dependencies

### ✅ **Testing Your Work:**
1. All services should show "healthy" status in `docker-compose ps`
2. Frontend should display existing todos from the database
3. You should be able to create and delete todos through the frontend
4. phpMyAdmin should allow you to browse the database tables

## Docker Compose Best Practices Demonstrated

### 1. **Service Dependencies with Health Checks**
```yaml
depends_on:
  database:
    condition: service_healthy
```
Ensures backend waits for database to be truly ready, not just started.

### 2. **Environment Variable Management**
```yaml
environment:
  DB_HOST: database  # Service name as hostname
  DB_USER: todouser
  DB_PASSWORD: todopass
```
Configuration through environment variables allows flexibility.

### 3. **Network Segmentation**
- `backend-network`: Database ↔ Backend ↔ phpMyAdmin
- `frontend-network`: Frontend ↔ Backend

### 4. **Volume Strategies**
- **Named volumes**: Persistent database storage
- **Bind mounts**: Static file serving

### 5. **Health Check Implementation**
```yaml
healthcheck:
  test: ["CMD", "mysqladmin", "ping", "-h", "localhost"]
  interval: 10s
  timeout: 5s
  retries: 5
```

## Comparison with Node.js Docker Example

Understanding the differences helps solidify concepts:

| Aspect | This Exercise (Java) | docker-example-app (Node.js) |
|--------|---------------------|------------------------------|
| **Backend Language** | Java with HTTP Server | Node.js with Express |
| **Database** | MySQL 8.0 | PostgreSQL 15 |
| **Frontend** | Static HTML + Vanilla JS | React with build process |
| **Database Admin** | phpMyAdmin | Adminer |
| **Build Process** | Maven (Java) | npm (Node.js) |
| **Base Images** | openjdk, mysql | node, postgres |
| **Configuration** | JDBC connection string | Node.js database client |

## Additional Resources

- [Docker Compose Documentation](https://docs.docker.com/compose/)
- [MySQL Docker Hub](https://hub.docker.com/_/mysql)
- [Java Database Connectivity (JDBC)](https://docs.oracle.com/javase/tutorial/jdbc/)
- [phpMyAdmin Documentation](https://docs.phpmyadmin.net/)

## Success Criteria

You've successfully completed the exercise when:

✅ All three services start without errors  
✅ `docker-compose ps` shows all services as "healthy"  
✅ Frontend loads at http://localhost:3000  
✅ You can view, add, and delete todos through the web interface  
✅ phpMyAdmin connects to the database at http://localhost:8081  
✅ Backend API responds to curl commands at http://localhost:8080  
✅ Database persists data between container restarts  

**Congratulations!** You've successfully orchestrated a complete full-stack application with Docker Compose, demonstrating service dependencies, network segmentation, database integration, and multi-container communication patterns essential for modern application deployment.