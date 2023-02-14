// def call(String name,String dayOfWeek) {
//     sh "echo Hello ${name}, today is ${dayOfWeek}"
// }

def call(Map config = [:]) {
    sh "echo Hello ${config.name}, today is ${config.dayOfWeek}"
}
