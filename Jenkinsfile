pipeline {
    agent any

    environment {
        DOCKER_COMPOSE_VERSION = '1.29.2'
    }

    stages {
        stage('Clone Repository') {
            steps {
                git 'https://github.com/oybek13/olvan-back.git'
            }
        }

        stage('Build Spring Boot App') {
            steps {
                sh './mvnw clean package -DskipTests'
            }
        }

        stage('Build Docker Image') {
            steps {
                sh 'docker build -t olvan-back-service-image .'
            }
        }

        stage('Start with Docker Compose') {
            steps {
                sh 'docker-compose down || true'
                sh 'docker-compose up -d --build'
            }
        }
    }

    post {
        always {
            echo "Cleaning up..."
            sh 'docker-compose down'
        }
    }
}
