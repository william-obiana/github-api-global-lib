def call(Map params){
    log.info params: params

    // command that is defined in our npmInstall.groovy
    def command = required(params, 'command')

    // directory containing package.json/package-lock.json, default '.'
    def projectHome = defaultIfNullOrEmpty(params['projectHome'], ".")

    // if we want detailed loglevel, default "false"
    def verbose = defaultIfNullOrEmpty(params['verbose'], "false")

    // cd to projectHome directory
    dir("$projectHome") {

        // if verbose is defined, add loglevel to the command
        if (verbose) {
            command = command + ' --loglevel silly'
        }

        // define the npmCommand to run on shell
        def npmCommand = sh(script: "npm "+command, returnStdout: true)

        // show the output
        log.info output: "npm "+command +" output: ${npmCommand}"
    }
}