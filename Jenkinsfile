#!/usr/bin/env groovy
pipeline {
    agent any

    parameters {
        string(name: 'lambdaName', defaultValue: '', description: 'Lambda name')
        choice(name: 'Account', choices: ['993058472258', '773974733061', '143936507261'])
        booleanParam(name: 'allLambdas', defaultValue: true, description: 'All lambdas')
        booleanParam(name: 'withLayer', defaultValue: false, description: 'Update layer')
    }

    stages {
        stage('Build') {
            steps {
                sh 'mvn clean package'
            }
        }
        stage('Deploy') {
            steps {
                withCredentials([[
                                         $class           : 'AmazonWebServicesCredentialsBinding',
                                         credentialsId    : params.Account,
                                         accessKeyVariable: 'AWS_ACCESS_KEY_ID',
                                         secretKeyVariable: 'AWS_SECRET_ACCESS_KEY']]) {
                    script {
                        def lambdaName = "all"
                        if (!params.allLambdas) {
                            lambdaName = params.lambdaName
                        }
                        if (params.withLayer) {
                            sh "chmod +x ./updateLambdasWithLayer.sh"
                            sh './updateLambdasWithLayer.sh ' + lambdaName + ' ' + params.Account
                        } else {
                            sh "chmod +x ./updateOnlyLambdasCode.sh"
                            sh './updateOnlyLambdasCode.sh ' + lambdaName + ' ' + params.Account
                        }
                    }
                }
            }
        }
    }
}