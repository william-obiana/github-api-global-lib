def call(Map config = [:]) {
    loadLinuxScript = (name: "hello-world.sh")
    sh "./hello-world.sh echo Hello ${config.name}, today is ${config.dayOfWeek}"
}

