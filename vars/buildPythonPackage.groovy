// prerequisites: Python, pip, openssl and AWS credentials should already set up in the Jenkins environment.
def call(String PYTHON_VERSION, String PACKAGE_DIR, String REQUIREMENTS_FILE, String S3_ARTIFACT_BUCKET_NAME, String S3_ARTIFACT_OUTPUT_PATH) {
    sh "env | sort"

    // checks if all the required arguments are provided
    if (PYTHON_VERSION == "" || PACKAGE_DIR == "" || REQUIREMENTS_FILE == "" || S3_ARTIFACT_BUCKET_NAME == "" || S3_ARTIFACT_OUTPUT_PATH == "") {
        echo "The following variables must be present when using this Project: PYTHON_VERSION, PACKAGE_DIR, REQUIREMENTS_FILE, S3_ARTIFACT_BUCKET_NAME, S3_ARTIFACT_OUTPUT_PATH"
        echo "Default values are set for the following: PYTHON_VERSION, PACKAGE_DIR (./), REQUIREMENTS_FILE (requirements.txt), S3_ARTIFACT_BUCKET_NAME (<account-id>-prd-operations-artifacts)"
        return 1
    }

    // if a target directory already exists, remove it
    if (fileExists("/target")) {
        sh "rm -rf /target"
    }

    // create target directory and copy the contents of the PACKAGE_DIR to the target directory
    sh "mkdir /target"
    sh "cp -a ${PACKAGE_DIR}/. /target/"

    // navigate to target directory and install dependencies using pip
    sh "cd /target"
    sh "pip install -r ./${REQUIREMENTS_FILE} -t ./ --quiet"

    // zip file and create SHA256 hash of the file (this is for lambda to pick up changes)
    sh "zip -r package.zip ."
    sh 'openssl dgst -sha256 -binary "package.zip" | openssl enc -A -base64 > "package.base64sha256"'

    // upload the zip file & hash file to S3
    sh "aws s3 cp --no-progress package.zip s3://${S3_ARTIFACT_BUCKET_NAME}/${S3_ARTIFACT_OUTPUT_PATH}/package.zip"
    sh "aws s3 cp --no-progress --content-type text/plain package.base64sha256 s3://${S3_ARTIFACT_BUCKET_NAME}/${S3_ARTIFACT_OUTPUT_PATH}/package.base64sha256"
}