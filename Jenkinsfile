// pipeline {
//     agent any
//
//     tools {
//        terraform 'Terraform'
//     }
//
//     stages {
//         stage('Terraform Init') {
//             steps {
//                 sh 'terraform version'
//                 sh 'terraform init'
//             }
//         }
//
//         stage('Terraform Plan') {
//             steps {
//                 sh 'terraform plan'
//             }
//         }
//     }
// }

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
                sh 'terraform init'
            }
        }

        stage('Terraform Plan') {
            steps {
                sh 'terraform plan'
            }
        }

        stage('Terraform Apply') {
            when {
                branch 'main'
            }
            steps {
                sh 'terraform apply -auto-approve'
            }
        }
    }
}