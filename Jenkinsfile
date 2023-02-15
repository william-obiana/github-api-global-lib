pipeline {
    agent any

    stages {
        stage('Install Terraform') {
            steps {
                sh 'curl -fsSL https://apt.releases.hashicorp.com/gpg | sudo apt-key add -'
                sh 'apt-add-repository "deb [arch=amd64] https://apt.releases.hashicorp.com $(lsb_release -cs) main"'
                sh 'apt-get update && apt-get install terraform'
                sh 'terraform -version'
            }
        }
        stage('Terraform apply') {
            steps {
                sh './terraform apply -auto-approve -no-color'
            }
        }
    }
}
