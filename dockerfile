# build
FROM eclipse-temurin:23-jdk AS build
WORKDIR /app

# copy necessary files
COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .
RUN ./mvnw dependency:go-offline
COPY src src
RUN ./mvnw clean package -DskipTests

# execute
FROM eclipse-temurin:23-jre-slim
WORKDIR /app

COPY --from=build /app/target/rock-paper-scissors-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]