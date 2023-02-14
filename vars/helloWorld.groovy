def call() {
    sh "echo Hello World"
}

def call2(String name, String dayOfWeek) {
    sh "echo Hello ${name}, today is ${dayOfWeek}"
}
