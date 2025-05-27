def registry = 'https://automationsaan.jfrog.io' // Artifactory base URL (no trailing slash)
def imageName = 'automationsaan.jfrog.io/hello-world-spring-docker-local/hello-world' // Docker image name with Artifactory repository path
def version   = '0.0.1-SNAPSHOT' // Version of the Docker image

def app // Define app variable globally for Docker build/publish stages

// Jenkins Declarative Pipeline definition
pipeline {
    agent {
        node {
            label 'maven' // Use Jenkins agent/node labeled 'maven' for builds
        }
    }
    environment {
        // Set JAVA_HOME and update PATH to use Java 21 and Maven 3.9.9
        JAVA_HOME = "/lib/jvm/java-21-openjdk-amd64"
        PATH = "/opt/apache-maven-3.9.9/bin:$JAVA_HOME/bin:$PATH"
    }
    stages {
        // Build stage: compiles code and runs all tests
        stage('Build') {
            steps {
                echo "JAVA VERSION:" // Print Java version for debugging
                sh 'java -version'
                echo "MAVEN VERSION:" // Print Maven version for debugging
                sh 'mvn -version'
                echo "----------- build started ----------"
                sh 'mvn clean verify' // Clean, compile, and run tests
                echo "----------- build completed ----------"
            }
        }
        // Test stage: generates test reports
        stage('Test') {
            steps {
                echo "----------- unit test started ----------"
                sh 'mvn surefire-report:report' // Generate Surefire test report
                echo "----------- unit test completed ----------"
            }
        }
        // SonarQube analysis stage: runs static code analysis
        stage('SonarQube analysis') {
            environment {
                scannerHome = tool 'sonar-scanner' // Use SonarQube scanner tool
            }
            steps {
                withSonarQubeEnv('sonarqube-server') { // Use SonarQube server credentials
                    echo "----------- SonarQube analysis started ----------"
                    sh "${scannerHome}/bin/sonar-scanner" // Run SonarQube analysis
                    echo "----------- SonarQube analysis completed ----------"
                }
            }
        }
        // Quality Gate stage: skipped due to SonarCloud premium requirement
        // stage('Quality Gate') {
        //     steps {
        //         echo "----------- Quality Gate started ----------"
        //         script {
        //             timeout(time: 15, unit: 'MINUTES') { // Wait up to 15 minutes
        //                 def qg = waitForQualityGate() // Get quality gate result
        //                 if (qg.status != 'OK') {
        //                     error "Pipeline aborted due to quality gate failure: ${qg.status}" // Fail if not OK
        //                 }
        //             }
        //         }
        //         echo "----------- Quality Gate completed ----------"
        //     }
        // }
        // Jar Publish stage: uploads the built JAR to Artifactory local repository
        stage('Jar Publish') {
            steps {
                script {
                    echo '<--------------- Jar Publish Started --------------->'
                    // Create Artifactory server object using the base URL and credentials
                    def server = Artifactory.newServer url:registry+"/artifactory", credentialsId:"jfrog-artifact-cred"
                    // Build properties for traceability (build ID and commit ID)
                    def properties = "buildid=${env.BUILD_ID},commitid=${GIT_COMMIT}";
                    // Upload spec: upload JAR(s) from target/ to the correct local repo in Artifactory
                    // 'flat: true' ensures the JAR is uploaded without the 'target/' directory in Artifactory
                    def uploadSpec = """
                        {
                          \"files\": [
                            {
                              \"pattern\": \"target/*.jar\",
                              \"target\": \"hello-world-spring-libs-release-local/\",
                              \"flat\": \"true\",
                              \"props\" : \"${properties}\",
                              \"exclusions\": [ \"*.sha1\", \"*.md5\"]
                            }
                          ]
                        }
                    """
                    def buildInfo = server.upload(uploadSpec) // Upload the JAR to Artifactory local repo
                    buildInfo.env.collect() // Collect environment info for traceability
                    server.publishBuildInfo(buildInfo) // Publish build info to Artifactory
                    echo '<--------------- Jar Publish Ended --------------->'
                }
            }
        }
        // Docker Build stage: builds the Docker image using the built JAR
        stage('Docker Build') {
            steps {
                script {
                    echo '<--------------- Docker Build Started --------------->'
                    // Build the Docker image and tag it with the version
                    app = docker.build(imageName+":"+version)
                    echo '<--------------- Docker Build Ends --------------->'
                }
            }
        }
        // Docker Publish stage: pushes the Docker image to Artifactory Docker registry
        stage('Docker Publish') {
            steps {
                script {
                    echo '<--------------- Docker Publish Started --------------->'
                    // Authenticate with the Docker registry and push the image
                    docker.withRegistry(registry, 'jfrog-artifact-cred') {
                        app.push() // Push the built Docker image to the registry
                    }
                    echo '<--------------- Docker Publish Ended --------------->'
                }
            }
        }
        // Helm Deploy stage: deploys the application using a Helm chart
        stage('Helm Deploy') {
            steps {
                script {
                    echo '<--------------- Helm Deploy Started --------------->'
                    // Install or upgrade the Helm release using the provided chart package
                    sh 'helm upgrade --install hello-world-springboot hello-world-springboot-0.1.0.tgz'
                    // 'upgrade --install' ensures idempotency: installs if not present, upgrades if already deployed
                    echo '<--------------- Helm Deploy Ended --------------->'
                }
            }
        }
        // Deploy stage: (commented out for future reference)
        // stage ("Deploy") {
        //     steps {
        //         script {
        //             echo '<--------------- Deploy Started --------------->'
        //             // Deploy the Docker image to the Kubernetes cluster
        //             sh './deploy.sh' // Execute the deployment script
        //             // Note: Ensure deploy.sh is executable and correctly configured for your environment
        //             echo '<--------------- Deploy Ended --------------->'
        //         }
        //     }
        // }
    }
}
// Pipeline end
//
// Pipeline Stages:
// - Build: Compiles and tests the code using Maven
// - Test: Generates test reports
// - SonarQube analysis: Runs static code analysis
// - Jar Publish: Uploads the built JAR to JFrog Artifactory
// - Docker Build: Builds the Docker image
// - Docker Publish: Pushes the Docker image to Artifactory Docker registry
// - Helm Deploy: Deploys the application to Kubernetes using Helm
//
// Notes:
// - Ensure 'ttrend-0.1.0.tgz' Helm chart is present in the workspace
// - Jenkins must have Docker, Helm, and required credentials configured
// - The pipeline uses best practices for traceability and secure artifact/image handling
