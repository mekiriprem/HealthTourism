# Use OpenJDK 17 Alpine as base image
FROM eclipse-temurin:17-jdk-alpine

# Set working directory inside the container
WORKDIR /app

# Copy the Spring Boot fat jar to the container
COPY target/hospialtourism.jar app.jar

# Expose port 8080 (default for Spring Boot)
EXPOSE 8080

# Run the Spring Boot app
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
