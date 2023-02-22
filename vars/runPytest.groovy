// prerequisites TBR: Python, pip, openssl and AWS credentials should already set up in the Jenkins environment.
def runPytest(String PYTHON_VERSION, String TEST_DIR, String REQUIREMENTS_FILE, String PYTEST_ARGS) {
    sh "env | sort"

    // checks if all the required arguments are provided
    if (PYTHON_VERSION == "" || TEST_DIR == "" || REQUIREMENTS_FILE == "") {
        echo "The following variables must be present when using this Project: PYTHON_VERSION, TEST_DIR, REQUIREMENTS_FILE"
        echo "The following variables are optional: PYTEST_ARGS"
        return 1
    }

    // navigate to TEST_DIR directory and install dependencies using pip
    sh "cd ${TEST_DIR}"
    sh "pip install -r ${REQUIREMENTS_FILE}"
    sh "pip install pytest"
    sh "pytest --junitxml=reports/report.xml ${PYTEST_ARGS}" // run pytest and generate reports

    // define a reports map for the report.xml
    def reports = junit testResults: 'reports/report.xml'
    step([$class: 'JUnitResultArchiver', testResults: 'reports/report.xml']) // use JUnitResultArchiver Plugin to archive
}