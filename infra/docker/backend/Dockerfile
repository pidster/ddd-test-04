# Base Image for Lakeside Mutual Backend Services
FROM eclipse-temurin:17-jre-alpine as base

# Set working directory
WORKDIR /app

# Add non-root user for security
RUN addgroup -S appgroup && adduser -S appuser -G appgroup
RUN chown -R appuser:appgroup /app

# Common environment variables
ENV SPRING_OUTPUT_ANSI_ENABLED=ALWAYS \
    JAVA_OPTS="-Xms512m -Xmx1024m"

# Use a multi-stage build to optimize the image
FROM maven:3.9.4-eclipse-temurin-17 as build

# Set working directory
WORKDIR /build

# Copy the Maven POM file and download dependencies
COPY pom.xml .
COPY ${SERVICE_NAME}/pom.xml ${SERVICE_NAME}/
RUN mvn -B dependency:go-offline -f ${SERVICE_NAME}/pom.xml -DskipTests

# Copy source code
COPY ${SERVICE_NAME}/src ${SERVICE_NAME}/src

# Build the application
RUN mvn -B clean package -f ${SERVICE_NAME}/pom.xml -DskipTests

# Final image
FROM base

# Copy the built jar file from the build stage
COPY --from=build /build/${SERVICE_NAME}/target/*.jar app.jar

# Set the startup command
USER appuser
ENTRYPOINT ["sh", "-c", "java ${JAVA_OPTS} -jar app.jar"]
