# Hello World Spring Boot Project

This repository contains a simple Spring Boot REST API with a Jenkins pipeline for CI/CD, SonarQube integration (using both Jenkins and local sonar-project.properties), and automated quality checks.

## Technologies Used

- **Java 21**: Programming language for the application.
- **Spring Boot 3.2.6**: Framework for building the REST API.
- **Maven**: Build automation and dependency management.
- **JUnit 5**: Unit testing framework.
- **Jenkins**: CI/CD automation server (pipeline defined in `Jenkinsfile`).
- **SonarQube/SonarCloud**: Static code analysis and quality gate enforcement.

## Project Structure

```
hello-world-springboot/
├── Jenkinsfile
├── pom.xml
├── sonar-project.properties
├── README.md
├── src/
│   ├── main/java/com/example/helloworldspringboot/
│   │   ├── HelloWorldSpringbootApplication.java
│   │   └── HelloController.java
│   └── test/java/com/example/helloworldspringboot/
│       └── HelloWorldSpringbootApplicationTests.java
```

## What the Project Does

- Exposes a REST endpoint at `/` that returns `Hello, World!`.
- Includes a unit test to verify the application context loads.
- Jenkins pipeline builds, tests, analyzes, and enforces code quality using SonarQube.
- Supports local SonarQube/SonarCloud analysis via `sonar-project.properties`.

## Setup and Run Locally

### Prerequisites
- Java 21 (JDK)
- Maven 3.9.9 or later
- (Optional) SonarQube CLI or SonarScanner for local analysis

### Build and Run

#### PowerShell
1. **Clone the repository:**
   ```powershell
   git clone <your-repo-url>
   cd hello-world-springboot
   ```
2. **Build the project:**
   ```powershell
   mvn clean verify
   ```
3. **Run the application:**
   ```powershell
   mvn spring-boot:run
   ```
4. **Access the API:**
   Open your browser or use curl to visit: [http://localhost:8080/](http://localhost:8080/)

#### Bash
1. **Clone the repository:**
   ```bash
   git clone <your-repo-url>
   cd hello-world-springboot
   ```
2. **Build the project:**
   ```bash
   mvn clean verify
   ```
3. **Run the application:**
   ```bash
   mvn spring-boot:run
   ```
4. **Access the API:**
   Open your browser or use curl to visit: http://localhost:8080/

### Run Tests
#### PowerShell
```powershell
mvn test
```
#### Bash
```bash
mvn test
```

### Run SonarQube/SonarCloud Analysis Locally

1. **Generate code coverage report:**
   ```powershell
   mvn test jacoco:report
   ```
2. **Run SonarQube analysis:**
   ```powershell
   sonar-scanner
   ```
   (Make sure SonarScanner is installed and available in your PATH.)

## Jenkins Pipeline

The `Jenkinsfile` defines the following stages:
- **Build**: Compiles and tests the code.
- **Test**: Generates test reports.
- **SonarQube analysis**: Runs static code analysis.
- **Quality Gate**: Waits for SonarQube quality gate result and fails if not passed.

> Ensure Jenkins is configured with a node labeled `maven`, Java 21, Maven 3.9.9, and SonarQube integration.

## SonarQube/SonarCloud
- The pipeline expects a SonarQube server named `sonarqube-server` and a scanner tool named `sonar-scanner` to be configured in Jenkins.
- For local analysis, the `sonar-project.properties` file is used and is compatible with SonarCloud.

## License
This project is for demonstration and educational purposes.
