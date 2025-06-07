# ---------- Stage 1: Build ----------
FROM maven:3.9.6-eclipse-temurin-17-alpine AS build
WORKDIR /app

# Copy all project files
COPY . .

# Build the project and skip tests
RUN mvn clean package -DskipTests

# ---------- Stage 2: Run ----------
FROM openjdk:17-jdk-alpine
WORKDIR /app

# Copy the jar file from the previous build stage
COPY --from=build /app/target/HospialTourism555-0.0.1-SNAPSHOT.jar app.jar

# Expose Spring Boot default port
EXPOSE 8080

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
