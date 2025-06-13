#!/usr/bin/env groovy
pipeline {
    agent any

    parameters {
        choice(name: 'REGION', choices: ['eu-central-1', 'eu-west-1'], description: 'Region to deploy')
        choice(name: 'ENV', choices: ['dev', 'test'], description: 'Environment to deploy')
        choice(name: 'ACTION', choices: ['apply', 'destroy'], description: 'Terraform action')
    }

    environment {
        AWS_REGION = "${params.REGION}"
        ECR_REPO = "143936507261.dkr.ecr.${params.REGION}.amazonaws.com/j3-repository-${params.REGION}-${params.ENV}"
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
                        rm -rf .terraform
                        terraform init -backend-config="key=j3-service/${params.REGION}/${params.ENV}/terraform.tfstate" -input=false -reconfigure

                        if [ "${params.ACTION}" = "apply" ]; then
                            echo "Planning Terraform apply"
                            terraform plan -var="env=${params.ENV}" -var="image_tag=${IMAGE_TAG}" -var="region=${params.REGION}" -input=false -out=tfplan
                            terraform show tfplan
                            terraform apply tfplan
                        else
                            echo "Planning Terraform destroy"
                            terraform plan -destroy -var="env=${params.ENV}" -var="region=${params.REGION}" -input=false -out=tfplan
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