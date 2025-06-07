# Start from OpenJDK 17 image
FROM openjdk:17-jdk-alpine

# Set working directory
WORKDIR /app

# Copy built jar file
COPY target/HospitalTourism-0.0.1-SNAPSHOT.jar app.jar

# Expose default Spring Boot port
EXPOSE 8080

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
