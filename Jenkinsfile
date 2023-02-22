@Library('shared-library') _

pipeline {
    agent any

    stages {
        stage('Build') {
            steps {
                helloWorldExternal(name: 'William', dayOfWeek: 'Thursday')
            }
        }
    }
}
