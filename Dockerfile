# Stage 1: Build the Spring Boot application
# Using Maven with Eclipse Temurin JDK 17 for backend build
FROM maven:3.8.6-eclipse-temurin-17 AS backend-build

# Set the working directory
WORKDIR /app

# Copy the Maven project files
COPY pom.xml .
COPY src ./src

# Build the application and skip tests using Maven
RUN mvn clean install -DskipTests
RUN mvn clean install -DskipTests

# Stage 2: Build the frontend assets
FROM node:16 AS frontend-build

# Set the working directory
WORKDIR /frontend

# Copy the frontend files
COPY package.json .
COPY package-lock.json .
COPY src ./src

# Install dependencies and build the frontend
RUN npm install
RUN npm run build
# Using Eclipse Temurin JRE 17 for the final image
FROM eclipse-temurin:17-jre
# Stage 3: Create the final image
FROM eclipse-temurin:17-jre

# Set the working directory
WORKDIR /app

# Copy the built Spring Boot JAR from the backend build stage
COPY --from=backend-build /app/target/demo-0.0.1-SNAPSHOT.jar app.jar

# Copy the built frontend assets from the frontend build stage
COPY --from=frontend-build /frontend/dist ./static

# Expose the application port
EXPOSE 8080

# Run the Spring Boot application
CMD ["java", "-jar", "app.jar"]