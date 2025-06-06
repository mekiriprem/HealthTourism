# STEP 1: Build the application
FROM maven:3.9.5-eclipse-temurin-17 AS builder

WORKDIR /app

# Copy Maven configuration first to cache dependencies
COPY pom.xml .
COPY src ./src

# Package the Spring Boot app (skip tests if needed)
RUN mvn clean package -DskipTests

# STEP 2: Run the application
FROM eclipse-temurin:17-jdk-alpine

WORKDIR /app

# Copy the JAR built in the previous stage
COPY --from=builder /app/target/*.jar app.jar

# Expose the default port
EXPOSE 8080

# Run the JAR
ENTRYPOINT ["java", "-jar", "app.jar"]
