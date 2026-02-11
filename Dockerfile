# ---------- Stage 1: Build the application ----------
FROM maven:3.9.9-eclipse-temurin-17 AS builder

WORKDIR /app

# Copy pom.xml and download dependencies first
COPY pom.xml .
RUN mvn -B -q -e -DskipTests dependency:go-offline

# Copy source code
COPY src ./src

# Build jar automatically
RUN mvn clean package -DskipTests


# ---------- Stage 2: Run the application ----------
FROM eclipse-temurin:17-jdk-alpine

WORKDIR /app

# Copy jar from builder stage
COPY --from=builder /app/target/*.jar app.jar

# Render dynamic port
EXPOSE 8080

# Run Spring Boot application
ENTRYPOINT ["java","-jar","/app/app.jar"]
