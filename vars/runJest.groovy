def call(String TEST_DIR, String JEST_ARGS, String TARGET = "/tmp/target") {
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
  def test_contents = libraryResource "${TEST_DIR}"
  writeFile file: "${TARGET}/${TEST_DIR}", text: test_contents

  // navigate to TEST_DIR directory and run Jest
  sh "npm init -y --prefix ${TARGET}/${TEST_DIR}"
  sh "npm install jest --save-dev --prefix ${TARGET}/${TEST_DIR}"
  sh "npm pkg set 'scripts.test'='jest' --prefix ${TARGET}/${TEST_DIR}"
  sh "npm test --prefix ${TARGET}/${TEST_DIR}"

  // define a reports map for the Jest XML report
  def reports = junit testResults: 'reports/test-results.xml'
  step([$class: 'JUnitResultArchiver', testResults: 'reports/test-results.xml']) // use JUnitResultArchiver Plugin to archive
}