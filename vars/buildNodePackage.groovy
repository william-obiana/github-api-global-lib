// prerequisites TBR: NPM, pip, zip, openssl, boto3 and AWS credentials should already set up in the Jenkins environment.
def call(String NODE_VERSION, String PACKAGE_DIR, String PACKAGE_FILE, String TARGET_DIR = "/tmp/target", String S3_ARTIFACT_BUCKET_NAME, String S3_ARTIFACT_OUTPUT_PATH) {
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
    sh "npm install --prefix ${TARGET_DIR}/${PACKAGE_DIR}"
    echo "Dependencies installed"

    // create tar file of package directory and place in target directory
    sh "tar -czf ${TARGET_DIR}/package.tar.gz ${TARGET_DIR}/${PACKAGE_DIR}"
    echo "Package created"

    // calculate SHA256 hash of the tar file (this is for S3 to pick up changes)
    sh 'openssl dgst -sha256 -binary "/tmp/target/package.tar.gz" | openssl enc -A -base64 > "/tmp/target/package.base64sha256"'
}