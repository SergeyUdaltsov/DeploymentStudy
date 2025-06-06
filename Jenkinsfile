#!/usr/bin/env groovy
pipeline {
    agent any

    environment {
        AWS_REGION = 'eu-central-1'
        ECR_REPO = '143936507261.dkr.ecr.eu-central-1.amazonaws.com/j3-study'
        IMAGE_TAG = "${env.BUILD_NUMBER}"
    }

    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Build') {
            steps {
                sh '''
                    echo "Making gradlew executable..."
                    chmod +x gradlew

                    echo "Building project with Gradle..."
                    ./gradlew clean build
                '''
            }
        }

        stage('Docker Build & Push') {
            steps {
                sh '''
                    aws sts get-caller-identity
                    echo "Building Docker image..."
                    docker build -t ${ECR_REPO}:${IMAGE_TAG} .

                    echo "Logging into AWS ECR..."
                    aws --region ${AWS_REGION} ecr get-login-password | \\
                        docker login --username AWS --password-stdin ${ECR_REPO}

                    echo "Pushing Docker image to ECR..."
                    docker push ${ECR_REPO}:${IMAGE_TAG}
                '''
            }
        }

        stage('Terraform Deploy') {
            steps {
                dir('terraform') {
                    sh '''
                        echo "🌍 Initializing Terraform"
                        terraform init -input=false

                        echo "🔍 Planning Terraform changes"
                        terraform plan -input=false -out=tfplan

                        echo "🚀 Applying Terraform"
                        terraform apply -input=false -auto-approve tfplan
                    '''
                }
            }
        }
    }

    post {
        always {
            echo 'Cleaning up local Docker image...'
            sh 'docker image rm ${ECR_REPO}:${IMAGE_TAG} || true'
        }
    }
}