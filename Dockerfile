# Use official OpenJDK image as the base
FROM openjdk:17-jdk-slim

# Set working directory inside the container
WORKDIR /app

# Copy the Spring Boot JAR file (make sure this matches your actual JAR name)
COPY target/HospialTourism.jar app.jar

# Set environment variable for IPv6 binding (Spring Boot should read this or be configured via properties)
ENV JAVA_OPTS="-Djava.net.preferIPv6Addresses=true"

# Expose port 8080 for host-to-container mapping
EXPOSE 8080

# Run the Spring Boot app
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]
