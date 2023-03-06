def call(Map params = [:]){

    // print entry message
    log.info(functionName: '-----npmInstall - Entry-----')

    // log the parameter on the agent
    log.info params: params

    // give the parameter a key:value
    params['command'] = 'install'

    // use the parameter when running the npmCommand()
    npmCommand(params)

    // print exit message
    log.info(functionName: '-----npmInstall - Exit-----')
}
