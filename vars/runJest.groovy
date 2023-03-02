def call(String NODE_VERSION, String TEST_DIR, String JEST_ARGS, String TARGET = "/tmp/target") {
    sh "env | sort"

    // checks if all the required arguments are provided
    if (TEST_DIR == "") {
        echo "The following variables must be present when using this Project: TEST_DIR"
        echo "The following variables are optional: JEST_ARGS"
        return 1
    }

    // if a test directory already exists, remove it
    if (fileExists(TEST_DIR)) {
        echo "Folder ${TEST_DIR} found!"
        sh "rm -rf ${TEST_DIR}"
    }

    // write file contents of the TEST_DIR to the test directory of agent
    def test_contents = libraryResource "${PACKAGE_DIR}/${PACKAGE_FILE}"
    writeFile file: "${TARGET}/${PACKAGE_DIR}/${TEST_DIR}/${PACKAGE_FILE}", text: test_contents

    // navigate to TEST_DIR directory and run Jest
    sh "npm init -y --prefix ${TARGET}/${PACKAGE_DIR}/${TEST_DIR}"
    sh "npm install jest --save-dev --prefix ${TARGET}/${PACKAGE_DIR}/${TEST_DIR}"
    sh "npm pkg set 'scripts.test'='jest' --prefix ${TARGET}/${PACKAGE_DIR}/${TEST_DIR}"
    echo "Jest installed"

    // run the test
    sh "npm test --prefix ${TARGET}/${PACKAGE_DIR}/${TEST_DIR} --junitxml=reports/report_${env.BUILD_ID}_${BUILD_TIMESTAMP}.xml ${JEST_ARGS}"

    // define a reports map for the Jest XML report
    def reports = junit testResults: 'reports/*.xml'
    step([$class: 'JUnitResultArchiver', testResults: 'reports/*.xml']) // use JUnitResultArchiver Plugin to archive
}