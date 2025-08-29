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