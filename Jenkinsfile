#!/usr/bin/env groovy
pipeline {
    agent any

    parameters {
        choice(name: 'ACTION', choices: ['apply', 'destroy'], description: 'Terraform action')
    }

    environment {
        AWS_REGION = 'eu-central-1'
        ECR_REPO = '143936507261.dkr.ecr.eu-central-1.amazonaws.com/j3-repository'
        IMAGE_TAG = "${env.BUILD_NUMBER}"
    }

    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Build') {
            when {
                expression { params.ACTION == 'apply'}
            }
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
            when {
                expression { params.ACTION == 'apply'}
            }
            steps {
                sh '''
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
                        export TF_LOG=DEBUG
                        export TF_LOG_PATH=terraform.log
                        echo "Initializing Terraform"
                        terraform init -input=false -force-copy

                        echo "Planning with IMAGE_TAG=${IMAGE_TAG}"
                        terraform plan -var="image_tag=${IMAGE_TAG}" -input=false -out=tfplan

                        echo "Applying Terraform"
                        terraform ${params.ACTION} -input=false -auto-approve tfplan
                        cat terraform.log
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