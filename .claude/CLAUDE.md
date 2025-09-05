# Docker Compose Exercise Evaluation Guide

## Exercise Overview - Docker Compose Todo Application

### Total: 20 points

**Note: Partial credit is available for each TODO. Students can receive partial points for attempting tasks even if not fully correct.**

This exercise evaluates students' understanding of Docker Compose orchestration, multi-service applications, networking, volumes, and service dependencies.

## Main Tasks (18 points)

### TODO 1: MySQL Database Service (6 points)

**Service Configuration (3 points)**
- Uses correct image: `mysql:8.0` (1 pt)
- Container name set: `todo-mysql-db` (0.5 pts)
- Port mapping correct: `3306:3306` (0.5 pts)
- Network assignment: `backend-network` (1 pt)

**Environment Variables (2 points)**
- `MYSQL_ROOT_PASSWORD: rootpass` (0.5 pts)
- `MYSQL_DATABASE: tododb` (0.5 pts)
- `MYSQL_USER: todouser` (0.5 pts)
- `MYSQL_PASSWORD: todopass` (0.5 pts)

**Volumes & Health Check (1 point)**
- Data persistence volume: `mysql_data:/var/lib/mysql` (0.5 pts)
- Health check implementation: `["CMD", "mysqladmin", "ping", "-h", "localhost"]` (0.5 pts)

### TODO 2: Java Backend Service (6 points)

**Build Configuration (2 points)**
- Build context: `./backend` (1 pt)
- Container name: `todo-java-backend` (0.5 pts)
- Port mapping: `8080:8080` (0.5 pts)

**Environment Variables (2 points)**
- `DB_HOST: database` (0.5 pts)
- `DB_PORT: 3306` (0.5 pts)
- `DB_NAME: tododb` (0.5 pts)
- `DB_USER: todouser` and `DB_PASSWORD: todopass` (0.5 pts)

**Dependencies & Networking (2 points)**
- Depends on database service with health condition (1 pt)
- Connected to both `backend-network` and `frontend-network` (1 pt)

### TODO 3: Frontend Service (6 points)

**Service Configuration (3 points)**
- Uses correct image: `nginx:alpine` (1 pt)
- Container name: `todo-frontend` (0.5 pts)
- Port mapping: `3000:80` (1.5 pts)

**Volume & Dependencies (2 points)**
- Volume mount: `./frontend:/usr/share/nginx/html:ro` (1 pt)
- Depends on backend service (1 pt)

**Networking (1 point)**
- Connected to `frontend-network` only (1 pt)

## Bonus Tasks (2 points)

### Advanced Configuration (1 point)
- Proper use of Docker Compose version (0.25 pts)
- Correct YAML formatting and structure (0.25 pts)
- Comments and documentation (0.25 pts)
- Error handling considerations (0.25 pts)

### Additional Features (1 point)
- Resource limits (memory, CPU) (0.5 pts)
- Additional environment configurations (0.5 pts)

## Evaluation Template

```markdown
# Docker Compose Exercise Evaluation Report

**Student:** [Student Name]  
**Email:** [student.email@gmail.com]  
**Date:** [Date]  
**Exercise:** Docker Compose Todo Application

## Score Summary
**TOTAL SCORE: X/20**

## Detailed Evaluation

### MAIN TASKS (X/18)

#### ✅/⚠️/❌ TODO 1 - MySQL Database Service (X/6 pts)
**Service Configuration (X/3 pts)**
- ✅/⚠️/❌ MySQL 8.0 image: [image used] (X pts)
- ✅/⚠️/❌ Container name: [name used] (X pts)
- ✅/⚠️/❌ Port mapping: [ports] (X pts)
- ✅/⚠️/❌ Network assignment: [network] (X pts)

**Environment Variables (X/2 pts)**
- ✅/⚠️/❌ MYSQL_ROOT_PASSWORD (X pts)
- ✅/⚠️/❌ MYSQL_DATABASE (X pts)
- ✅/⚠️/❌ MYSQL_USER (X pts)
- ✅/⚠️/❌ MYSQL_PASSWORD (X pts)

**Volumes & Health Check (X/1 pts)**
- ✅/⚠️/❌ Data persistence volume (X pts)
- ✅/⚠️/❌ Health check implementation (X pts)

**Comments:** [Specific feedback on database service implementation]

#### ✅/⚠️/❌ TODO 2 - Java Backend Service (X/6 pts)
**Build Configuration (X/2 pts)**
- ✅/⚠️/❌ Build context: [context used] (X pts)
- ✅/⚠️/❌ Container name: [name used] (X pts)
- ✅/⚠️/❌ Port mapping: [ports] (X pts)

**Environment Variables (X/2 pts)**
- ✅/⚠️/❌ DB_HOST configuration (X pts)
- ✅/⚠️/❌ DB_PORT configuration (X pts)
- ✅/⚠️/❌ DB_NAME configuration (X pts)
- ✅/⚠️/❌ DB_USER and DB_PASSWORD (X pts)

**Dependencies & Networking (X/2 pts)**
- ✅/⚠️/❌ Database dependency with health condition (X pts)
- ✅/⚠️/❌ Network configuration (backend + frontend) (X pts)

**Comments:** [Specific feedback on backend service implementation]

#### ✅/⚠️/❌ TODO 3 - Frontend Service (X/6 pts)
**Service Configuration (X/3 pts)**
- ✅/⚠️/❌ Nginx Alpine image: [image used] (X pts)
- ✅/⚠️/❌ Container name: [name used] (X pts)
- ✅/⚠️/❌ Port mapping: [ports] (X pts)

**Volume & Dependencies (X/2 pts)**
- ✅/⚠️/❌ Frontend volume mount (X pts)
- ✅/⚠️/❌ Backend dependency (X pts)

**Networking (X/1 pts)**
- ✅/⚠️/❌ Frontend network only (X pts)

**Comments:** [Specific feedback on frontend service implementation]

### BONUS TASKS (X/2)

#### ✅/⚠️/❌ Advanced Configuration (X/1 pts)
- ✅/⚠️/❌ Docker Compose version and structure
**Comments:** [Specific feedback]

#### ✅/⚠️/❌ Additional Features (X/1 pts)
- ✅/⚠️/❌ Resource limits or extra configurations
**Comments:** [Specific feedback]

## Feedback

### Strengths:
- [List key strengths in Docker Compose implementation]

### Areas for Improvement:
- [List areas needing work]

### Recommendations:
[Numbered list of specific recommendations for improvement]

## Grade: **[Letter]** ([Description])
[Overall assessment of student understanding and Docker Compose implementation]
```

## Grading Rubric Details

### Excellent (18-20 points)
- All TODOs completed correctly with proper service orchestration
- Correct networking and service dependencies
- Proper volume management and health checks
- Clean, well-structured YAML
- Shows understanding of Docker Compose best practices

### Good (14-17 points)
- Most TODOs completed correctly
- Services can communicate properly
- Minor issues in configuration or networking
- Basic understanding demonstrated

### Satisfactory (10-13 points)
- Services partially configured
- Some networking or dependency issues
- Application may not run correctly
- Basic Docker Compose concepts understood

### Needs Improvement (<10 points)
- Multiple TODOs incomplete or incorrect
- Services cannot communicate
- Major networking or configuration issues
- Fundamental misunderstanding of Docker Compose

## Common Mistakes & Deductions

1. **Incorrect service names** (-0.5 pts each)
   - Not matching required container names

2. **Network misconfiguration** (-1 pt each)
   - Services not in correct networks
   - Missing network assignments

3. **Port mapping errors** (-1 pt each)
   - Incorrect host:container port mapping
   - Missing port assignments

4. **Environment variable issues** (-0.5 pts each)
   - Missing or incorrect database connection variables
   - Wrong values for database credentials

5. **Dependency problems** (-1 pt each)
   - Missing depends_on relationships
   - Not using health check conditions

6. **Volume mounting errors** (-1 pt)
   - Incorrect volume syntax
   - Missing data persistence

## Quick Evaluation Checklist

```bash
# Test commands to run for evaluation:
docker-compose config --quiet  # Validate YAML syntax
docker-compose up --build      # Test full stack deployment
docker-compose ps              # Check service status
docker-compose logs            # Review service logs
curl http://localhost:3000     # Test frontend accessibility
curl http://localhost:8080     # Test backend API
curl http://localhost:8081     # Test phpMyAdmin access
```

## Evaluation Directory
All student evaluations should be saved in `../docker-eval` directory, to be appended to the ex1-evaluation-<student_name>.md file.
If the ex1 file does not exist, create a new one called ex2-evaluation-<student_name>.md.


## Docker Compose Concepts Tested

1. **Service Definition**: Understanding how to define multiple services
2. **Networking**: Creating and using custom networks for service isolation
3. **Dependencies**: Using depends_on with health check conditions
4. **Volumes**: Persistent data storage and file mounting
5. **Environment Variables**: Service configuration and inter-service communication
6. **Port Mapping**: Exposing services to host system
7. **Health Checks**: Service monitoring and dependency management
8. **Build Context**: Building custom images within compose
9. **YAML Structure**: Proper Docker Compose file formatting

## Important Notes for Evaluators

- Test the complete application stack, not just individual services
- Verify that services can communicate with each other
- Check that data persists after container restarts
- Ensure proper network isolation (frontend should not access database directly)
- Validate that all required ports are accessible from host
- Review YAML syntax and structure quality