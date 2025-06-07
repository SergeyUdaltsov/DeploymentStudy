#!/usr/bin/env groovy
pipeline {
    agent any

    environment {
        AWS_REGION = 'eu-central-1'
        ECR_REPO = '143936507261.dkr.ecr.eu-central-1.amazonaws.com/j3-study'
        IMAGE_TAG = "${env.BUILD_NUMBER}"
    }

    stages {
        stage('Build') {
            steps {
                sh './gradlew clean build'
            }
        }
        stage('Deploy') {
            steps {
              sh """
                  docker build -t ${ECR_REPO}:${IMAGE_TAG} .
                  aws --region ${AWS_REGION} ecr get-login-password | docker login --username AWS --password-stdin ${ECR_REPO}
                  docker push ${ECR_REPO}:${IMAGE_TAG}
              """
            }
        }
    }
}