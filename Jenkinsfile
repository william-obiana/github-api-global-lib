pipeline {
    agent any

    tools {
       terraform 'terraform'
    }

    stages {
        stage('Terraform Init') {
            steps {
                sh 'terraform -version'
                sh 'terraform init'
            }
        }

        stage('Terraform Plan') {
            steps {
                sh 'terraform plan'
            }
        }
    }
}
