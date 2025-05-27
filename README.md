# Automationsaan Hello World Spring Boot Project

This repository contains a simple Spring Boot REST API with a complete CI/CD pipeline using Jenkins, SonarQube/SonarCloud, Docker, JFrog Artifactory, and Kubernetes (EKS) for automated deployment, quality checks, and autoscaling. The project demonstrates modern DevOps best practices, including secure configuration and cloud-native deployment.

## Technologies Used

- **Java 21**: Programming language for the application.
- **Spring Boot 3.2.6**: Framework for building the REST API.
- **Maven**: Build automation and dependency management.
- **JUnit 5**: Unit testing framework.
- **Jenkins**: CI/CD automation server (pipeline defined in `Jenkinsfile`).
- **SonarQube/SonarCloud**: Static code analysis and quality gate enforcement.
- **Docker**: Containerization of the Spring Boot application.
- **JFrog Artifactory**: Artifact and Docker image repository.
- **Kubernetes**: Container orchestration for automated deployment, scaling, and management.
- **Amazon EKS (Elastic Kubernetes Service)**: Managed Kubernetes cluster for running production workloads in AWS.
- **Kubernetes Autoscaling**: Horizontal Pod Autoscaler (HPA) for automatic scaling based on resource usage.
- **Kubernetes YAML Manifests**: Declarative configuration for deployment, service, namespace, and secrets.
- **Shell Scripts**: For automated deployment to Kubernetes clusters.

## Project Structure

```
hello-world-springboot/
├── Dockerfile
├── Jenkinsfile
├── pom.xml
├── sonar-project.properties
├── README.md
├── deploy.sh
├── deployment.yaml
├── namespace.yaml
├── secret.yaml
├── service.yaml
├── src/
│   ├── main/java/com/example/helloworldspringboot/
│   │   ├── HelloWorldSpringbootApplication.java
│   │   └── HelloController.java
│   └── test/java/com/example/helloworldspringboot/
│       └── HelloWorldSpringbootApplicationTests.java
├── target/
│   └── hello-world-springboot-0.0.1-SNAPSHOT.jar
```

## What the Project Does

- Exposes a REST endpoint at `/` that returns `Hello, World!`.
- Includes a unit test to verify the application context loads.
- Jenkins pipeline builds, tests, analyzes, and enforces code quality using SonarQube/SonarCloud.
- Publishes the built JAR to JFrog Artifactory.
- Builds and publishes a Docker image to JFrog Artifactory Docker registry.
- Supports local SonarQube/SonarCloud analysis via `sonar-project.properties`.
- Deploys the application to Kubernetes (EKS) using declarative YAML manifests.
- Supports Kubernetes autoscaling and secure secret management.

## Setup and Run Locally

### Prerequisites
- Java 21 (JDK)
- Maven 3.9.9 or later
- Docker (for containerization)
- (Optional) SonarQube CLI or SonarScanner for local analysis
- kubectl (Kubernetes command-line tool)
- AWS CLI (for EKS deployment)

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

### Build and Run with Docker
1. **Build the JAR:**
   ```powershell
   mvn clean package
   ```
2. **Build the Docker image:**
   ```powershell
   docker build -t hello-world-app .
   ```
3. **Run the Docker container:**
   ```powershell
   docker run -p 8080:8080 hello-world-app
   ```
4. **Access the API:**
   Open your browser or use curl to visit: [http://localhost:8080/](http://localhost:8080/)

### Deploy to Kubernetes (EKS)
1. **Update `kubeconfig` for EKS:**
   ```powershell
   aws eks --region <your-region> update-kubeconfig --name <your-cluster-name>
   ```
2. **Ensure `deploy.sh` is executable:**
   ```powershell
   chmod +x deploy.sh
   ```
3. **Create the namespace:**
   ```powershell
   kubectl apply -f namespace.yaml
   ```
4. **Create the secret:**
   ```powershell
   kubectl apply -f secret.yaml
   ```
5. **Deploy the application:**
   ```powershell
   kubectl apply -f deployment.yaml
   ```
6. **Expose the service:**
   ```powershell
   kubectl apply -f service.yaml
   ```
7. **Access the application:**
   Get the service URL:
   ```powershell
   kubectl get svc -n hello-world
   ```
   Open the URL in your browser.

## Jenkins Pipeline

The `Jenkinsfile` defines the following stages:
- **Build**: Compiles and tests the code.
- **Test**: Generates test reports.
- **SonarQube analysis**: Runs static code analysis.
- **Quality Gate**: (Commented out/skipped if using SonarCloud free tier)
- **Jar Publish**: Uploads the built JAR to JFrog Artifactory.
- **Docker Build**: Builds the Docker image using the built JAR.
- **Docker Publish**: Pushes the Docker image to JFrog Artifactory Docker registry.
- **Kubernetes Deploy**: Deploys the application to EKS.

> Ensure Jenkins is configured with a node labeled `maven`, Java 21, Maven 3.9.9, Docker, and SonarQube/SonarCloud integration.

## SonarQube/SonarCloud
- The pipeline expects a SonarQube server named `sonarqube-server` and a scanner tool named `sonar-scanner` to be configured in Jenkins.
- For local analysis, the `sonar-project.properties` file is used and is compatible with SonarCloud.
- The Quality Gate stage is commented out by default for SonarCloud free tier users.

## Dockerfile
- The Dockerfile uses the official OpenJDK 21 image.
- It copies the built JAR from `target/hello-world-springboot-0.0.1-SNAPSHOT.jar` to `hello-world-app.jar` in the image.
- The container runs the JAR using `ENTRYPOINT ["java", "-jar", "/hello-world-app.jar"]`.

## License
This project is for demonstration and educational purposes.
