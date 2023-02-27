def call(String NODE_VERSION, String PACKAGE_DIR, String PACKAGE_FILE, String TARGET_DIR, String S3_ARTIFACT_BUCKET_NAME, String S3_ARTIFACT_OUTPUT_PATH) {
    sh "env | sort"

    // checks if all the required arguments are provided
    if (NODE_VERSION == "" || PACKAGE_DIR == "" || PACKAGE_FILE == "" || TARGET_DIR == "" || S3_ARTIFACT_BUCKET_NAME == "" || S3_ARTIFACT_OUTPUT_PATH == "") {
        echo "The following variables must be present when using this Project: NODE_VERSION, PACKAGE_DIR, PACKAGE_FILE, TARGET_DIR, S3_ARTIFACT_BUCKET_NAME, S3_ARTIFACT_OUTPUT_PATH"
        echo "Default values are set for the following: NODE_VERSION (14.x), PACKAGE_DIR (./), TARGET_DIR (/tmp/target)"
        return 1
    }

    // if a target directory already exists, remove it
    if (fileExists(TARGET_DIR)) {
        echo "Folder ${TARGET_DIR} found!"
        sh "rm -rf ${TARGET_DIR}"
    }

    // create target directory
    sh "mkdir ${TARGET_DIR}"

    // write file contents of the PACKAGE_DIR to the target directory of agent
    def packagecontents = libraryResource "${PACKAGE_DIR}/${PACKAGE_FILE}"
    writeFile file: "${TARGET_DIR}/${PACKAGE_DIR}/${PACKAGE_FILE}", text: packagecontents

    // navigate to PACKAGE_DIR directory and install dependencies using npm
    sh "cd ${TARGET_DIR}/${PACKAGE_DIR} && npm install"
    echo "Dependencies installed"

    // create tar file of package directory and move to target directory
    sh "cd ${TARGET_DIR} && tar -czf package.tar.gz ${PACKAGE_DIR}"
    echo "Package created"

    // calculate SHA256 hash of the tar file (this is for S3 to pick up changes)
    sh 'cd ${TARGET_DIR} && openssl dgst -sha256 -binary "package.tar.gz" | openssl enc -A -base64 > "package.base64sha256"'

    // upload package to S3
    sh "aws s3 cp ${TARGET_DIR}/package.tar.gz s3://${S3_ARTIFACT_BUCKET_NAME}/${S3_ARTIFACT_OUTPUT_PATH}/ --grants read=uri=http://acs.amazonaws.com/groups/global/AllUsers"
    sh "aws s3 cp ${TARGET_DIR}/package.base64sha256 s3://${S3_ARTIFACT_BUCKET_NAME}/${S3_ARTIFACT_OUTPUT_PATH}/ --grants read=uri=http://acs.amazonaws.com/groups/global/AllUsers"
    echo "Package uploaded to S3"
}