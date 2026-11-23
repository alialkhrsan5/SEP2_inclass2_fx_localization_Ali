pipeline {
    agent any

    tools {
        maven 'MAVEN_HOME'
    }

    environment {
        DOCKER_IMAGE = 'alii123x/temp-fx-localization'
        DOCKER = 'C:\\Users\\alial\\AppData\\Local\\Programs\\DockerDesktop\\resources\\bin\\docker.exe'
        SONARQUBE_SERVER = 'SonarQubeServer'
        SCANNER_HOME = tool 'SonarScanner'
    }

    stages {
        stage('Checkout') {
            steps {
                git branch: 'master', url: 'https://github.com/alialkhrsan5/SEP2_inclass2_fx_localization_Ali.git'
            }
        }

        stage('Build') {
            steps {
                bat 'mvn clean install'
            }
        }

        stage('Test') {
            steps {
                bat 'mvn test'
            }
        }

        stage('Code Coverage') {
            steps {
                bat 'mvn jacoco:report'
            }
        }

        stage('SonarQube Analysis') {
            steps {
                withCredentials([string(credentialsId: 'SONAR_TOKEN', variable: 'SONAR_TOKEN')]) {
                    withSonarQubeEnv("${env.SONARQUBE_SERVER}") {
                        bat """
                          "%SCANNER_HOME%\\bin\\sonar-scanner.bat" ^
                          -Dsonar.projectKey=temp-fx-localization ^
                          -Dsonar.projectName=temp-fx-localization ^
                          -Dsonar.sources=src/main ^
                          -Dsonar.tests=src/test ^
                          -Dsonar.java.binaries=target/classes ^
                          -Dsonar.java.test.binaries=target/test-classes ^
                          -Dsonar.coverage.jacoco.xmlReportPaths=target/site/jacoco/jacoco.xml ^
                          -Dsonar.host.url=http://localhost:9000 ^
                          -Dsonar.token=%SONAR_TOKEN%
                        """
                    }
                }
            }
        }

        stage('Publish Test Results') {
            steps {
                junit '**/target/surefire-reports/*.xml'
            }
        }

        stage('Publish Coverage Report') {
            steps {
                jacoco()
            }
        }

        stage('Docker Build') {
            steps {
                bat "%DOCKER% build -t %DOCKER_IMAGE%:%BUILD_NUMBER% -t %DOCKER_IMAGE%:latest ."
            }
        }

        stage('Docker Push') {
            steps {
                withCredentials([usernamePassword(credentialsId: 'dockerhub', usernameVariable: 'DOCKER_USER', passwordVariable: 'DOCKER_PASS')]) {
                    bat "%DOCKER% login -u %DOCKER_USER% -p %DOCKER_PASS%"
                    bat "%DOCKER% push %DOCKER_IMAGE%:%BUILD_NUMBER%"
                    bat "%DOCKER% push %DOCKER_IMAGE%:latest"
                }
            }
        }
    }
}
