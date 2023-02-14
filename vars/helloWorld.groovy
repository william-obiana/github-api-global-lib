def call(Map config = [:]) {
    sh "echo Hello ${config.name}, today is ${config.dayOfWeek}"
}
