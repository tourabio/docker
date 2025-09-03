# syntax=docker/dockerfile:1

### Build stage (Java 11 + Maven)
FROM maven:3.9.6-eclipse-temurin-11 AS build
WORKDIR /build

COPY pom.xml .
RUN mvn -q -DskipTests dependency:go-offline

COPY src ./src
# build + copie des dépendances dans target/libs
RUN mvn -q -DskipTests clean package dependency:copy-dependencies -DoutputDirectory=target/libs

### Runtime stage (JRE 11)
FROM eclipse-temurin:11-jre
# sécurité: non-root
RUN useradd -u 10001 -m appuser
USER appuser

WORKDIR /app
# jar appli + libs
COPY --from=build /build/target/*.jar /app/
COPY --from=build /build/target/libs /app/libs

EXPOSE 8080
ENV JAVA_OPTS=""

# lance avec le classpath (jar + libs)
ENTRYPOINT ["sh","-c","exec java $JAVA_OPTS -cp \"$(ls /app/*.jar):/app/libs/*\" com.example.todoapp.TodoApplication"]
