# Spring Boot File Server

This is a simple Spring Boot file server application with RESTful API capabilities. It allows users to upload, download, and retrieve a list of uploaded files.


## How to Run the Application

### Prerequisites
- Java 17
- Maven
- Docker (if running in a Docker container)

### Steps

1. Clone the repository:
   ```bash
   git clone https://github.com/purvish25/motorola.git
   cd motorola (For linux)

2. Build the application:
   ```bash
   mvn clean install (For Linux)
   ./mvnw clean install (For windows)

3. Run the application:
   ```bash
   java -jar target/motorola-0.0.1-SNAPSHOT.jar

### How to Run Tests

1. Make sure you have the prerequisites installed.
2. Run the tests:
   ```bash
   mvn test (For Linux)
   ./mvnw test (For windows)

### Docker Support

1. Build the Docker image:
   ```bash
   docker build -t motorola-app .

2. Run the Docker container:
   ```bash
   docker run -p 8080:8080 motorola-app
- The application will be accessible at http://localhost:8080/api/files.

