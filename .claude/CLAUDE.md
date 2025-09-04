# Docker Exercise Evaluation Guide

## ex1-mark - Java Todo Application Dockerfile

### Total: 20 points

**Note: Partial credit is available for each TODO. Students can receive partial points for attempting tasks even if not fully correct.**

#### Main Tasks (14 points)

##### TODO 1: Base Image Selection (3 points)
- **Correct Java 11 image (3 pts)**
  - Uses openjdk:11 or equivalent (2 pts)
  - Slim/Alpine variant preferred for optimization (1 pt)
  - Partial credit: 1 pt for any Java image, 2 pts for Java 11

##### TODO 2: Working Directory (2 points)
- **WORKDIR set correctly (2 pts)**
  - Uses /app or similar logical path
  - Consistent throughout Dockerfile
  - Partial credit: 1 pt for setting WORKDIR even if path is suboptimal

##### TODO 3: Maven Build (4 points)
- **Build approach (2 pts)**
  - Maven installed or using maven image
  - Correct mvn clean package command
  - Partial credit: 1 pt for attempting build even with errors
- **Build optimization (2 pts)**
  - Dependencies cached properly (pom.xml copied first)
  - Multi-stage build approach gets full points
  - Partial credit: 1 pt for basic build without optimization

##### TODO 4: Port Exposure (2 points)
- **Correct port exposed (2 pts)**
  - Must check TodoApplication.java for actual port
  - Uses EXPOSE directive correctly
  - Partial credit: 1 pt for using EXPOSE with wrong port

##### TODO 5: Run Command (3 points)
- **CMD/ENTRYPOINT correct (2 pts)**
  - Proper Java execution command
  - Correct JAR file path (target/todo-app-1.0.0.jar)
  - Partial credit: 1 pt for attempting java command with errors
- **Command format (1 pt)**
  - Uses exec form ["java", "-jar", ...]
  - Or shell form with proper syntax
  - Partial credit: 0.5 pt for working command with suboptimal format

#### Bonus Tasks (6 points)

##### Health Check (2 points)
- **HEALTHCHECK implementation (2 pts)**
  - Proper health check command
  - Appropriate intervals and timeouts

##### Multi-stage Build (2 points)
- **Build optimization (2 pts)**
  - Separate build and runtime stages
  - Minimal final image size

##### Security - Non-root User (2 points)
- **User configuration (2 pts)**
  - Creates and uses non-root user
  - Proper permissions set

### Evaluation Template

```
Student: [Name]
Date: [Date]
Exercise: Java Todo Application Dockerfile

MAIN TASKS (14/14):
□ TODO 1 - Base Image (3 pts)
  ✓/✗ Uses Java 11 image: ___________
  ✓/✗ Optimal image choice (slim/jre)
  
□ TODO 2 - Working Directory (2 pts)
  ✓/✗ WORKDIR set: ___________
  
□ TODO 3 - Maven Build (4 pts)
  ✓/✗ Build command correct
  ✓/✗ Dependencies cached properly
  ✓/✗ Multi-stage approach
  
□ TODO 4 - Port Exposure (2 pts)
  ✓/✗ Correct port exposed: ___________
  
□ TODO 5 - Run Command (3 pts)
  ✓/✗ Java command correct
  ✓/✗ Proper command format

BONUS TASKS (6/6):
□ Health Check (2 pts)
  ✓/✗ Implemented: ___________
  
□ Multi-stage Build (2 pts)
  ✓/✗ Implemented: ___________
  
□ Non-root User (2 pts)
  ✓/✗ Implemented: ___________

TOTAL SCORE: ___/20

FEEDBACK:
Strengths:
- 

Areas for Improvement:
- 

Common Issues to Check:
- 
```

### Grading Rubric Details

#### Excellent (18-20 points)
- All TODOs completed correctly
- At least 2 bonus tasks implemented
- Clean, optimized Dockerfile
- Shows understanding of Docker best practices

#### Good (14-17 points)
- All main TODOs completed
- Minor issues in implementation
- Some bonus features attempted

#### Satisfactory (10-13 points)
- Most TODOs completed
- Application runs but not optimized
- Basic understanding demonstrated

#### Needs Improvement (<10 points)
- Multiple TODOs incomplete or incorrect
- Application doesn't build/run
- Major conceptual misunderstandings

### Common Mistakes & Deductions

1. **Wrong Java version** (-2 pts)
   - Not using Java 11 as specified

2. **No build optimization** (-1 pt)
   - Copying all files before pom.xml
   - Not utilizing layer caching

3. **Security issues** (-1 pt each)
   - Running as root when not needed
   - Exposing unnecessary ports
   - Including build tools in final image

4. **Incorrect CMD format** (-1 pt)
   - Wrong JAR path
   - Missing java command flags

### Quick Evaluation Checklist

```bash
# Test commands to run for evaluation:
docker build -t todo-app .
docker run -p 8080:8080 todo-app
docker images todo-app  # Check image size
docker run todo-app whoami  # Check if non-root user
```

## student-emails

inesabdellaoui111@gmail.com
hamzabelarbi36@gmail.com
nazihbenbrahim9@gmail.com
hamzabenhmida27058@gmail.com
bouazizghada19@gmail.com
ferdaousbouatay@gmail.com
rahmaboufaied5@gmail.com
ghaidacheebi@gmail.com
eyadorai179@gmail.com
ranimelheni63@gmail.com
rawyag20@gmail.com
elyes1kouki@gmail.com
medhedimaaroufi@gmail.com
messaadifiras204@gmail.com
rayenmalouche27@gmail.com
arijneffati02@gmail.com
mazenbensaid548@gmail.com
rachdimedamine@gmail.com
sayadiademali335@gmail.com
raiesnsayar4@gmail.com
hamedslim15@gmail.com
molkatoubale23@gmail.com
oussematouhami1@gmail.com
oumyma65@gmail.com
nesrinebentmessek@gmail.com
mahdiabcha90@gmail.com
jed.abidi@gmail.com
arifamohamedyassine123@gmail.com
baatoutchahrazed@gmail.com
karimenbenromdhane55@gmail.com
houssembensalah53@gmail.com
amine.bouabid2018@gmail.com
esrabrahmii@gmail.com
chouchenanis998@gmail.com
fatmagaida52@gmail.com
guesmiyosra0@gmail.com
hajamormaryem38@gmail.com
chaimaharzali333@gmail.com
louaykahouli@gmail.com
ktari610@gmail.com
ghofranelakhal2002@gmail.com
maalej.alaeddine123@gmail.com
tasnimmeriah1@gmail.com
mezzieya665@gmail.com
khalafnakbi2002@gmail.com
weslatizouhour@gmail.com
inestmimi1234@gmail.com
oussamatrzd19@gmail.com
nourheneyoussfi03@gmail.com