# Use Maven to build the application
FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /app

# Copy pom.xml and download dependencies (cached layer)
COPY backend/pom.xml .
RUN mvn dependency:go-offline -B

# Copy source code and build
COPY backend/src ./src
RUN mvn clean package -DskipTests

# Runtime stage
FROM eclipse-temurin:17-jre
WORKDIR /app

# Copy the built JAR from build stage
COPY --from=build /app/target/cafe-finder-0.0.1-SNAPSHOT.jar app.jar

# Expose port (Render will set PORT env var)
EXPOSE 8082

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]

