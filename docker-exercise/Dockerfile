# Docker Exercise: Complete the Dockerfile for the Java Todo Application
# 
# Instructions for students:
# 1. Choose an appropriate base image for a Java application
# 2. Set the working directory
# 3. Copy the necessary files
# 4. Build the application
# 5. Expose the correct port
# 6. Define the command to run the application
  #///////////////////////////créer une image Docker pour ton application///////////
# TODO 1: Use an appropriate Java base image (hint: Todo Application is using java 11)


#//////une image de base qui contient Maven + Java 11////////
#AS builder crée un stage nommé "builder" : c’est le conteneur qui servira uniquement à construire l’application.
FROM maven:3.8.5-openjdk-11 AS builder

# TODO 2: Set the working directory inside the container (e.g., /app)

#//////////définit /app comme dossier de travail dans le conteneur
WORKDIR /app

# Copy Maven files for dependency caching
# (This is done before copying source code for better Docker layer caching)

#//////////copie uniquement le fichier pom.xml (la configuration Maven).
#//////Astuce : ça permet à Docker de mettre en cache le téléchargement des dépendances.
COPY pom.xml .

# Copy the source code
COPY src ./src

# TODO 3: Install Maven and build the application
# Hint: You might need to:
# - Install Maven if not in base image
# - Run: mvn clean package
# - Or use a multi-stage build approach


#/////On construit l’application avec Maven :
#/////clean : nettoie les anciens builds
#////package : compile et génère un fichier .jar dans target/
#///////DskipTests : accélère le build en sautant les tests.
RUN mvn clean package -DskipTests 




#/////Docker exécute les processus en root (peu sécurisé).
#///////Ici on crée un utilisateur appuser avec un groupe appgroup, puis on change les permissions du dossier /app.
# Create non-root user
RUN addgroup --system appgroup && adduser --system appuser --ingroup appgroup
RUN chown -R appuser:appgroup /app
USER appuser

# TODO 4: Expose the port that the application runs on (check TodoApplication.java for the port number)
EXPOSE 8080



#//////Vérifie toutes les 30 secondes si l’application répond sur http://localhost:8080/.
#////////////Si l’app ne répond pas → le conteneur est marqué comme unhealthy.
# Add a health check (basic: app responds on port 8080)
HEALTHCHECK --interval=30s --timeout=5s --start-period=10s --retries=3 \
CMD curl -f http://localhost:8080/ || exit 1

# TODO 5: Define the command to run the Java application
# Hint: java -jar target/todo-app-1.0.0.jar
# Or if dependencies are separate: java -cp "target/*:target/lib/*" com.example.todoapp.TodoApplication


#////////C’est la commande par défaut qui sera exécutée quand on lance le conteneur.
#//////Ici tu dois remplacer app.jar par le vrai nom du jar généré (ex: target/todo-app-1.0.0.jar).
#/////Sinon ton conteneur ne démarrera pas.
CMD ["java", "-jar", "target/todo-app-1.0.0.jar"]

# BONUS TODO: 
# - Add a health check
# - Use multi-stage build to reduce image size
# - Add a non-root user for better security