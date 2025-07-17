# Stage 1: Build with Maven (larger image)
FROM ubuntu:latest AS build
RUN apt-get update && apt-get install -y maven

# Update environment variables for the current shell session (optional - see note below)
ENV MAVEN_HOME Program Files/apache-maven-3.9.6
ENV PATH="$PATH:$MAVEN_HOME/bin"

COPY pom.xml .
RUN mvn clean package -DskipTests

# Stage 2: Final image (smaller)
FROM openjdk:17-slim
COPY --from=build /target/crudApi-0.0.1-SNAPSHOT.jar crudapi.jar
EXPOSE 9898
ENTRYPOINT ["java", "-jar", "crudapi.jar"]
