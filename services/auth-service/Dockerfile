FROM openjdk:17-jdk-slim

# Set working directory
WORKDIR /app

# Copy JAR file into container
ARG JAR_FILE=target/*jar
COPY ./target/*.jar app.jar

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]