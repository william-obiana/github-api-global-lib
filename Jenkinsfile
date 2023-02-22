@Library('shared-library') _

// pipeline {
//     agent any
//
//     stages {
//         stage('Build') {
//             steps {
//                 helloWorldExternal(name: 'William', dayOfWeek: 'Thursday')
//             }
//         }
//     }
// }

pipeline {
    agent any

//     // add the environment variables in Jenkins UI and reference them in this Jenkinsfile
//     environment {
//         PYTHON_VERSION = "${PYTHON_VERSION}"
//         PACKAGE_DIR = "${PACKAGE_DIR}"
//         REQUIREMENTS_FILE = "${REQUIREMENTS_FILE}"
//         S3_ARTIFACT_BUCKET_NAME = "${S3_ARTIFACT_BUCKET_NAME}"
//         S3_ARTIFACT_OUTPUT_PATH = "${S3_ARTIFACT_OUTPUT_PATH}"
//         TEST_DIR = "${TEST_DIR}"
//         PYTEST_ARGS = "${PYTEST_ARGS}"
//     }

    stages {
        stage('Build Python Package') {
            steps {
                script {
                    buildPythonPackage(
                        "3.8",
                        libraryResource "lambda/lambda_function.py",
                        "requirements.txt",
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


