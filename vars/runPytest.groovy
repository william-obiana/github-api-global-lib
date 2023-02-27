// prerequisites TBR: Python3, pip, zip, openssl, boto3 and AWS credentials should already set up in the Jenkins environment.
def call(String PYTHON_VERSION, String TEST_DIR, String REQUIREMENTS_FILE, String PYTEST_ARGS, String TARGET = "/tmp/target") {
    sh "env | sort"

    // checks if all the required arguments are provided
    if (PYTHON_VERSION == "" || TEST_DIR == "" || REQUIREMENTS_FILE == "") {
        echo "The following variables must be present when using this Project: PYTHON_VERSION, TEST_DIR, REQUIREMENTS_FILE"
        echo "The following variables are optional: PYTEST_ARGS"
        return 1
    }

    // if a test directory already exists, remove it
    if (fileExists(TEST_DIR)) {
        echo "Folder ${TEST_DIR} found!"
        sh "rm -rf ${TEST_DIR}"
    }

    // write file contents of the PACKAGE_DIR to the test directory of agent
    def test_package_contents = libraryResource "${PACKAGE_DIR}/${PACKAGE_FILE}"
    writeFile file: "${TARGET}/${TEST_DIR}/${PACKAGE_FILE}", text: test_package_contents

    // write file contents of the REQUIREMENTS_FILE to the test directory of agent
    def test_requirements_contents = libraryResource "${PACKAGE_DIR}/${REQUIREMENTS_FILE}"
    writeFile file: "${TARGET}/${TEST_DIR}/${REQUIREMENTS_FILE}", text: test_requirements_contents

    // install dependencies using pip (boto3, pytest)
    sh "pip install -r ${TARGET}/${TEST_DIR}/${REQUIREMENTS_FILE} -t ./ --quiet"
    echo "Requirements installed"

    // navigate to TEST_DIR directory and run pytest
    sh "pytest ${TARGET}/${TEST_DIR}/${PACKAGE_FILE} --junitxml=reports/report_${BUILD_TIMESTAMP}.xml ${PYTEST_ARGS}" // run pytest and generate reports

    // define a reports map for the report.xml
    def reports = junit testResults: 'reports/*.xml'
    step([$class: 'JUnitResultArchiver', testResults: 'reports/*.xml']) // use JUnitResultArchiver Plugin to archive
}