def registry = 'https://automationsaan.jfrog.io' // Artifactory base URL (no trailing slash)
def imageName = 'automationsaan.jfrog.io/hello-world-spring-docker-local/hello-world' // Docker image name with Artifactory repository path
def version   = '0.0.1-SNAPSHOT' // Version of the Docker image

def app // Define app variable globally for Docker build/publish stages

pipeline {
    agent {
        node {
            label 'maven' // Use Jenkins agent/node labeled 'maven' for builds
        }
    }
    environment {
        JAVA_HOME = "/lib/jvm/java-21-openjdk-amd64"
        PATH = "/opt/apache-maven-3.9.9/bin:$JAVA_HOME/bin:$PATH"
    }
    stages {
        stage('Build') {
            steps {
                echo "JAVA VERSION:"
                sh 'java -version'
                echo "MAVEN VERSION:"
                sh 'mvn -version'
                echo "----------- build started ----------"
                sh 'mvn clean verify'
                echo "----------- build completed ----------"
            }
        }
        stage('Test') {
            steps {
                echo "----------- unit test started ----------"
                sh 'mvn surefire-report:report'
                echo "----------- unit test completed ----------"
            }
        }
        stage('SonarQube analysis') {
            environment {
                scannerHome = tool 'sonar-scanner'
            }
            steps {
                withSonarQubeEnv('sonarqube-server') {
                    echo "----------- SonarQube analysis started ----------"
                    sh "${scannerHome}/bin/sonar-scanner"
                    echo "----------- SonarQube analysis completed ----------"
                }
            }
        }
        stage('Jar Publish') {
            steps {
                script {
                    echo '<--------------- Jar Publish Started --------------->'
                    def server = Artifactory.newServer url:registry+"/artifactory", credentialsId:"jfrog-artifact-cred"
                    def properties = "buildid=${env.BUILD_ID},commitid=${GIT_COMMIT}";
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
                    def buildInfo = server.upload(uploadSpec)
                    buildInfo.env.collect()
                    server.publishBuildInfo(buildInfo)
                    echo '<--------------- Jar Publish Ended --------------->'
                }
            }
        }
        stage('Docker Build') {
            steps {
                script {
                    echo '<--------------- Docker Build Started --------------->'
                    // Debug: Print current user and groups to verify Docker access
                    sh 'whoami && groups'
                    app = docker.build(imageName+":"+version)
                    echo '<--------------- Docker Build Ends --------------->'
                }
            }
        }
        stage('Docker Publish') {
            steps {
                script {
                    echo '<--------------- Docker Publish Started --------------->'
                    docker.withRegistry(registry, 'jfrog-artifact-cred') {
                        app.push()
                    }
                    echo '<--------------- Docker Publish Ended --------------->'
                }
            }
        }
    }
}
