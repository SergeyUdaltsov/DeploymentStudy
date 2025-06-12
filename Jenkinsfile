#!/usr/bin/env groovy
pipeline {
    agent any

    parameters {
        choice(name: 'ACTION', choices: ['apply', 'destroy'], description: 'Terraform action')
        choice(name: 'ENV', choices: ['dev', 'test'], description: 'Environment to deploy')
    }

    environment {
        AWS_REGION = 'eu-central-1'
        ECR_REPO = '143936507261.dkr.ecr.eu-central-1.amazonaws.com/j3-repository-${params.ENV}'
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
                dir('terraform/service') {
                    sh """
                        echo "Initializing Terraform"
                        terraform init -backend-config="key=service/eu-central-1/${params.ENV}/terraform.tfstate" -input=false -force-copy

                        if [ "${params.ACTION}" = "apply" ]; then
                            echo "Planning Terraform apply"
                            terraform plan -var="env=${params.ENV}" -var="image_tag=${IMAGE_TAG}" -input=false -out=tfplan
                            terraform show tfplan
                            terraform apply tfplan
                        else
                            echo "Planning Terraform destroy"
                            terraform destroy -var="env=${params.ENV}" -input=false -out=tfplan
                            terraform show tfplan
                            terraform apply tfplan
                        fi
                    """
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