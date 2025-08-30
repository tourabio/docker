# Docker Compose Todo Application

A full-stack Todo application demonstrating Docker Compose orchestration with multiple services.

## Architecture

```
┌─────────────┐     ┌─────────────┐     ┌──────────────┐
│   Frontend  │────▶│   Backend   │────▶│   Database   │
│   (React)   │     │  (Node.js)  │     │ (PostgreSQL) │
│  Port: 3001 │     │  Port: 3000 │     │  Port: 5432  │
└─────────────┘     └─────────────┘     └──────────────┘
                                              │
                                         ┌────▼────┐
                                         │ Adminer │
                                         │Port:8080│
                                         └─────────┘
```

## Services

### 1. **Database (PostgreSQL)**
- Lightweight PostgreSQL 15 Alpine image
- Persistent data storage with Docker volumes
- Automatic initialization with sample data
- Health checks for service readiness

### 2. **Backend API (Node.js + Express)**
- RESTful API with full CRUD operations
- PostgreSQL integration with connection pooling
- CORS enabled for frontend communication
- Automatic database retry logic
- Health endpoint for monitoring

### 3. **Frontend (React)**
- Modern React UI with hooks
- Real-time CRUD operations
- Responsive design
- Production-ready nginx server

### 4. **Adminer (Database UI)**
- Web-based database management
- Access at http://localhost:8080
- Pre-configured for PostgreSQL connection

## Prerequisites

- Docker Desktop installed
- Docker Compose (included with Docker Desktop)
- 2GB+ free RAM
- Ports 3000, 3001, 5432, 8080 available

## Quick Start

1. **Clone or navigate to the project directory**
```bash
cd docker-example-app
```

2. **Start all services**
```bash
docker-compose up -d
```

3. **Access the applications**
- Frontend: http://localhost:3001
- Backend API: http://localhost:3000
- Database UI: http://localhost:8080
  - Server: `database`
  - Username: `todouser`
  - Password: `todopass`
  - Database: `tododb`

## Docker Compose Commands

### Basic Operations
```bash
# Start all services
docker-compose up -d

# Stop all services
docker-compose down

# View logs
docker-compose logs -f

# View specific service logs
docker-compose logs -f backend
docker-compose logs -f frontend
docker-compose logs -f database

# Restart a specific service
docker-compose restart backend
```

### Development Mode
```bash
# Start with live logs
docker-compose up

# Rebuild images after code changes
docker-compose up --build

# Remove everything including volumes
docker-compose down -v
```

### Service Management
```bash
# Check service status
docker-compose ps

# Execute commands in running containers
docker-compose exec backend npm run dev
docker-compose exec database psql -U todouser -d tododb

# Scale services (if configured)
docker-compose up -d --scale backend=3
```

## API Endpoints

### Backend API (http://localhost:3000)

| Method | Endpoint | Description | Body |
|--------|----------|-------------|------|
| GET | `/` | API info | - |
| GET | `/health` | Health check | - |
| GET | `/todos` | Get all todos | - |
| GET | `/todos/:id` | Get specific todo | - |
| POST | `/todos` | Create todo | `{ "task": "...", "description": "..." }` |
| PUT | `/todos/:id` | Update todo | `{ "task": "...", "description": "...", "completed": boolean }` |
| DELETE | `/todos/:id` | Delete todo | - |

### Testing with curl
```bash
# Get all todos
curl http://localhost:3000/todos

# Create a todo
curl -X POST http://localhost:3000/todos \
  -H "Content-Type: application/json" \
  -d '{"task": "Learn Docker Compose", "description": "Master multi-container apps"}'

# Update a todo
curl -X PUT http://localhost:3000/todos/1 \
  -H "Content-Type: application/json" \
  -d '{"completed": true}'

# Delete a todo
curl -X DELETE http://localhost:3000/todos/1
```

## Environment Variables

Configure in `.env` file:

```env
# Database
DB_USER=todouser
DB_PASSWORD=todopass
DB_NAME=tododb

# Application
NODE_ENV=development
REACT_APP_API_URL=http://localhost:3000
```

## Project Structure

```
docker-example-app/
├── docker-compose.yaml      # Orchestration configuration
├── .env                     # Environment variables
├── backend/
│   ├── Dockerfile          # Backend container configuration
│   ├── package.json        # Node.js dependencies
│   └── app.js             # Express server with PostgreSQL
├── frontend/
│   ├── Dockerfile          # Frontend container configuration
│   ├── package.json        # React dependencies
│   ├── public/
│   └── src/
│       ├── App.js         # Main React component
│       ├── App.css        # Styling
│       └── index.js       # React entry point
└── db/
    └── init.sql           # Database initialization script
```

## Docker Compose Features Demonstrated

### 1. **Service Dependencies**
```yaml
depends_on:
  database:
    condition: service_healthy
```
Backend waits for database to be healthy before starting.

### 2. **Health Checks**
```yaml
healthcheck:
  test: ["CMD-SHELL", "pg_isready -U ${DB_USER:-todouser}"]
```
Ensures services are actually ready, not just started.

### 3. **Volumes**
- **Named volumes**: Persistent database storage
- **Bind mounts**: Development hot-reload

### 4. **Networks**
Custom bridge network for inter-service communication.

### 5. **Environment Variables**
- `.env` file support
- Service-specific environment configuration
- Default values with overrides

### 6. **Build Context**
```yaml
build:
  context: ./backend
  dockerfile: Dockerfile
```

## Teaching Points

### For Students Learning Docker Compose:

1. **Service Orchestration**
   - How multiple containers work together
   - Service discovery through container names
   - Network isolation and communication

2. **Database Integration**
   - Connection strings using service names
   - Initialization scripts
   - Data persistence with volumes

3. **Development Workflow**
   - Hot-reload with volume mounts
   - Log aggregation
   - Environment-specific configurations

4. **Production Considerations**
   - Multi-stage builds (frontend)
   - Health checks
   - Resource limits (can be added)
   - Security (non-root users can be implemented)

5. **Best Practices**
   - Separation of concerns
   - Environment variables for configuration
   - Proper error handling
   - Graceful shutdowns

## Troubleshooting

### Common Issues

1. **Port already in use**
```bash
# Find process using port
lsof -i :3000  # Mac/Linux
netstat -ano | findstr :3000  # Windows

# Change port in docker-compose.yaml
ports:
  - "3005:3000"  # Map to different host port
```

2. **Database connection failed**
```bash
# Check database logs
docker-compose logs database

# Verify database is running
docker-compose exec database pg_isready

# Reset database
docker-compose down -v
docker-compose up -d
```

3. **Frontend can't reach backend**
- Check REACT_APP_API_URL in .env
- Verify CORS settings in backend
- Check network connectivity

4. **Changes not reflecting**
```bash
# Rebuild images
docker-compose build --no-cache
docker-compose up -d
```

## Additional Resources

- [Docker Compose Documentation](https://docs.docker.com/compose/)
- [PostgreSQL Docker Hub](https://hub.docker.com/_/postgres)
- [Node.js Best Practices](https://github.com/goldbergyoni/nodebestpractices)
- [React Documentation](https://reactjs.org/)

## Docker Compose YAML Deep Dive

This section explains every key aspect of our `docker-compose.yaml` file for educational purposes.

### File Structure Overview

```yaml
version: '3.8'         # Compose file format version
services:              # Define all containers
networks:              # Define custom networks
volumes:               # Define persistent storage
```

### Service Definitions

#### 1. **Database Service**

```yaml
database:
  image: postgres:15-alpine                    # Lightweight PostgreSQL image
  container_name: todo-db                      # Custom container name
  environment:                                 # Environment variables
    POSTGRES_USER: ${DB_USER:-todouser}       # Uses .env or default value
    POSTGRES_PASSWORD: ${DB_PASSWORD:-todopass}
    POSTGRES_DB: ${DB_NAME:-tododb}
  ports:
    - "5432:5432"                              # Host:Container port mapping
  volumes:
    - postgres_data:/var/lib/postgresql/data  # Named volume for data persistence
    - ./db/init.sql:/docker-entrypoint-initdb.d/init.sql  # Init script
  networks:
    - backend-db-network                      # Isolated network for backend
    - db-admin-network                        # Isolated network for adminer
  healthcheck:                                 # Container health monitoring
    test: ["CMD-SHELL", "pg_isready -U ${DB_USER:-todouser}"]
    interval: 10s                              # Check every 10 seconds
    timeout: 5s                                # Timeout after 5 seconds
    retries: 5                                 # Mark unhealthy after 5 failures
```

**Key Learning Points:**
- **Alpine images**: Smaller, more secure Linux distribution
- **Named volumes**: Data persists even when container is removed
- **Init scripts**: Automatically run SQL files on first startup
- **Health checks**: Ensure service is ready, not just running
- **Network isolation**: Database on multiple networks for different consumers

#### 2. **Backend Service**

```yaml
backend:
  build:                                       # Build from Dockerfile
    context: ./backend                        # Build context directory
    dockerfile: Dockerfile                     # Dockerfile name
  container_name: todo-backend
  environment:
    NODE_ENV: ${NODE_ENV:-development}
    PORT: 3000
    DB_HOST: database                         # Service name as hostname
    DB_PORT: 5432
    DB_USER: ${DB_USER:-todouser}
    DB_PASSWORD: ${DB_PASSWORD:-todopass}
    DB_NAME: ${DB_NAME:-tododb}
  ports:
    - "3000:3000"
  depends_on:                                  # Service dependencies
    database:
      condition: service_healthy              # Wait for health check
  networks:
    - frontend-backend-network                # Share network with frontend
    - backend-db-network                      # Share network with database
  volumes:
    - ./backend:/usr/src/app                  # Bind mount for hot-reload
    - /usr/src/app/node_modules              # Anonymous volume for node_modules
  command: npm start                          # Override CMD from Dockerfile
```

**Key Learning Points:**
- **Build context**: Local build instead of pulling image
- **Service discovery**: Use service names as hostnames
- **depends_on with condition**: Advanced dependency management
- **Multiple networks**: Backend as bridge between frontend and database
- **Volume tricks**: Prevent host node_modules from overriding container's

#### 3. **Frontend Service**

```yaml
frontend:
  build:
    context: ./frontend
    dockerfile: Dockerfile
  container_name: todo-frontend
  environment:
    REACT_APP_API_URL: http://localhost:3000  # React env variable
  ports:
    - "3001:3000"                             # Different host port
  depends_on:
    - backend                                  # Simple dependency
  networks:
    - frontend-backend-network                # Only backend network access
  volumes:
    - ./frontend:/app
    - /app/node_modules
```

**Key Learning Points:**
- **Port mapping**: Container port 3000 mapped to host port 3001
- **Network isolation**: Frontend can't directly access database
- **React env vars**: Must start with REACT_APP_
- **Simple dependency**: No health check condition

#### 4. **Adminer Service**

```yaml
adminer:
  image: adminer:4.8.1                        # Specific version tag
  container_name: todo-adminer
  ports:
    - "8080:8080"
  depends_on:
    - database
  networks:
    - db-admin-network                        # Isolated admin network
  environment:
    ADMINER_DEFAULT_SERVER: database
    ADMINER_DEFAULT_DB: tododb
    ADMINER_DESIGN: pepa-linha               # UI theme
```

**Key Learning Points:**
- **Version pinning**: Using specific version for stability
- **Network isolation**: Admin tool separated from application
- **Pre-configuration**: Default values for easier access

### Network Configuration

```yaml
networks:
  frontend-backend-network:
    driver: bridge                            # Default driver
    name: frontend-backend-net                # Custom network name
  backend-db-network:
    driver: bridge
    name: backend-database-net
  db-admin-network:
    driver: bridge
    name: database-admin-net
```

**Network Segmentation Strategy:**
- **frontend-backend-network**: Frontend ↔ Backend communication
- **backend-db-network**: Backend ↔ Database communication  
- **db-admin-network**: Adminer ↔ Database communication

**Security Benefits:**
- Frontend cannot directly query database
- Adminer cannot access application services
- Principle of least privilege enforced

### Volume Configuration

```yaml
volumes:
  postgres_data:                              # Named volume declaration
```

**Volume Types Used:**
1. **Named Volume** (`postgres_data`): Managed by Docker, persists data
2. **Bind Mounts** (`./backend:/usr/src/app`): Links host directory for development
3. **Anonymous Volume** (`/usr/src/app/node_modules`): Prevents conflicts

### Environment Variable Patterns

```yaml
${DB_USER:-todouser}                         # Syntax explanation
```
- `${DB_USER}`: Use value from .env file
- `:-todouser`: Default value if not set
- Allows configuration without modifying docker-compose.yaml

### Docker Compose Commands Explained

```bash
# Start all services in detached mode
docker-compose up -d

# View real-time logs from all services
docker-compose logs -f

# Rebuild images before starting
docker-compose up --build

# Stop and remove containers, networks (keeps volumes)
docker-compose down

# Stop and remove everything including volumes
docker-compose down -v

# View service status
docker-compose ps

# Execute command in running container
docker-compose exec backend sh

# Restart specific service
docker-compose restart backend

# View specific service logs
docker-compose logs -f database
```

### Best Practices Demonstrated

1. **Service Naming**: Clear, descriptive names
2. **Network Segmentation**: Security through isolation
3. **Health Checks**: Ensure service readiness
4. **Environment Variables**: Configuration flexibility
5. **Volume Strategy**: Persistence and development needs
6. **Dependency Management**: Proper startup order
7. **Port Management**: Avoid conflicts with different host ports
8. **Version Pinning**: Reproducible builds
9. **Build Context**: Efficient layer caching
10. **Comments**: Document complex configurations

### Common Modifications for Learning

1. **Add Resource Limits:**
```yaml
services:
  backend:
    deploy:
      resources:
        limits:
          cpus: '0.5'
          memory: 512M
```

2. **Add Restart Policies:**
```yaml
services:
  backend:
    restart: unless-stopped
```

3. **Add Logging Configuration:**
```yaml
services:
  backend:
    logging:
      driver: json-file
      options:
        max-size: "10m"
        max-file: "3"
```

4. **Scale Services:**
```bash
docker-compose up -d --scale backend=3
```
(Note: Requires removing container_name and adjusting port mapping)