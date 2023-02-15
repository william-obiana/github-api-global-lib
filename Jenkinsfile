pipeline {
    agent any

    stages {
        stage('Install Terraform') {
            steps {
                sh 'echo terraform -version'
            }
        }
        stage('Terraform apply') {
            steps {
                sh './terraform apply -auto-approve -no-color'
            }
        }
    }
}
