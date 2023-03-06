@Library('shared-library') _

pipeline {
    agent any

    stages {
        stage('Npm Install') {
            steps {
                script {
                    npmInstall('Install')
                }
            }
        }
    }
}

