@Library('shared-library') _

pipeline {
    agent any

    stages {
        stage('Npm Install') {
            steps {
                script {
                    npmInstall(
                        NODE_VERSION,
                        PACKAGE_DIR,
                        PACKAGE_FILE
                    )
                }
            }
        }
    }
}

