@Library('shared-library') _

pipeline {
    agent any

    // add the environment variables in Jenkins UI and reference them in this Jenkinsfile
    environment {
        PYTHON_VERSION = "3.8"
        PACKAGE_DIR = 'lambda/lambda_function.py'
        REQUIREMENTS_FILE = 'lambda/requirements.txt'
        S3_ARTIFACT_BUCKET_NAME = "${S3_ARTIFACT_BUCKET_NAME}"
        S3_ARTIFACT_OUTPUT_PATH = "${S3_ARTIFACT_OUTPUT_PATH}"
        TEST_DIR = "tests/requirements.txt"
        PYTEST_ARGS = "--verbose"
    }

    stages {
        stage('Build Python Package') {
            steps {
                script {
                    buildPythonPackage(
                        PYTHON_VERSION,
                        PACKAGE_DIR,
                        REQUIREMENTS_FILE,
                        S3_ARTIFACT_BUCKET_NAME,
                        S3_ARTIFACT_OUTPUT_PATH
                    )
                }
            }
        }
        stage('Run Pytest') {
            steps {
                script {
                    runPytest(
                        PYTHON_VERSION,
                        TEST_DIR,
                        REQUIREMENTS_FILE,
                        PYTEST_ARGS
                    )
                }
            }
        }
    }
}


