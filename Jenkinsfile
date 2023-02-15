pipeline {
    agent {
        docker {
            image 'hashicorp/terraform:1.1.0'
            args '-v /var/run/docker.sock:/var/run/docker.sock'
        }
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
