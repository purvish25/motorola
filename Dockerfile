# Build stage
FROM maven:latest AS build
WORKDIR /app
COPY . .
RUN mvn clean install

# Final stage
# Use an OpenJDK runtime base image
FROM openjdk:latest
# Set the working directory
WORKDIR /app

COPY --from=build /app/target/motorola-*.jar /app/app.jar
EXPOSE 8080
CMD ["java", "-jar", "app.jar"]