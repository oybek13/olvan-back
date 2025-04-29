pipeline {
    agent any

    environment {
        DOCKER_COMPOSE_VERSION = '1.29.2'
    }

    stages {
        stage('Clone Repository') {
            steps {
                git branch: 'main', url: 'https://github.com/oybek13/olvan-back.git'
            }
        }

        stage('Build Spring Boot App') {
            steps {
                sh 'chmod +x mvnw'
                sh './mvnw clean package -DskipTests'
            }
        }

        stage('Build Docker Image') {
            steps {
                sh 'docker rmi -f olvan-back-service-image || true'
                def tag = "olvan-back-service-image:${BUILD_NUMBER}"
                sh "docker build -t ${tag} ."
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
            sh 'docker image prune -f'
        }
    }
}
