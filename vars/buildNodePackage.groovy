// prerequisites: node, npm, nano, curl, zip, boto3 and AWS credentials should already set up in the Jenkins environment.
def call(String NODE_VERSION, String PACKAGE_DIR, String PACKAGE_FILE, String TARGET = "/tmp/target") {
    sh "env | sort"

    // checks if all the required arguments are provided
    if (NODE_VERSION == "" || PACKAGE_DIR == "" || PACKAGE_FILE == "" || TARGET == "") {
        echo "The following variables must be present when using this Project: NODE_VERSION, PACKAGE_DIR, PACKAGE_FILE, TARGET"
        echo "Default values are set for the following: NODE_VERSION (14.x), PACKAGE_DIR (./), TARGET (/tmp/target)"
        return 1
    }

    // if a target directory already exists, remove it
    if (fileExists(TARGET)) {
        echo "Folder ${TARGET} found!"
        sh "rm -rf ${TARGET}"
    }

    // create target directory
    sh "mkdir ${TARGET}"

    // write file contents of the PACKAGE_DIR to the target directory of agent
    def packagecontents = libraryResource "${PACKAGE_DIR}/${PACKAGE_FILE}"
    writeFile file: "${TARGET}/${PACKAGE_DIR}/${PACKAGE_FILE}", text: packagecontents

    // create tar file of package directory and place in target directory
    sh "tar -czf ${TARGET}/package.tar.gz ${TARGET}/${PACKAGE_DIR}"
    echo "Package archived"

    // calculate SHA256 hash of the tar file (this is for S3 to pick up changes)
    sh 'openssl dgst -sha256 -binary "/tmp/target/package.tar.gz" | openssl enc -A -base64 > "/tmp/target/package.base64sha256"'
}