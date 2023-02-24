// prerequisites TBR: Python, pip, zip, openssl and AWS credentials should already set up in the Jenkins environment.
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

    sh "cp ${TARGET}/${REQUIREMENTS_FILE} ${TARGET}/${TEST_DIR}"
    sh "echo completed"

    // navigate to TEST_DIR directory and install dependencies using pip
    sh "cd ${TEST_DIR}"
    sh "pip install -r ${REQUIREMENTS_FILE}"
    sh "pip install pytest"
    sh "pytest --junitxml=reports/report.xml ${PYTEST_ARGS}" // run pytest and generate reports

    // define a reports map for the report.xml
    def reports = junit testResults: 'reports/report.xml'
    step([$class: 'JUnitResultArchiver', testResults: 'reports/report.xml']) // use JUnitResultArchiver Plugin to archive
}