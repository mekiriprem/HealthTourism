# Stage 1: Build the JAR
FROM maven:3.9.6-eclipse-temurin-17 AS build
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

# Stage 2: Run the app
FROM eclipse-temurin:17
WORKDIR /app
COPY --from=build /app/target/	.jar app.jar
EXPOSE 4545
ENTRYPOINT ["java", "-jar", "app.jar"]
