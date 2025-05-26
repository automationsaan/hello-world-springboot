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
        // Quality Gate stage: waits for SonarQube quality gate result
        stage('Quality Gate') {
            steps {
                echo "----------- Quality Gate started ----------"
                script {
                    timeout(time: 15, unit: 'MINUTES') { // Wait up to 15 minutes
                        def qg = waitForQualityGate() // Get quality gate result
                        if (qg.status != 'OK') {
                            error "Pipeline aborted due to quality gate failure: ${qg.status}" // Fail if not OK
                        }
                    }
                }
                echo "----------- Quality Gate completed ----------"
            }
        }
    }
}
