tasks.register<JavaExec>("contractTest") {
    dependsOn(tasks.testClasses)
    classpath = sourceSets.test.get().runtimeClasspath
    mainClass.set("dev.brenopoubel.apoio.SupportTest")
}

// The suite is a standalone Java main, with no JUnit engine to discover.
tasks.test {
    enabled = false
    dependsOn("contractTest")
}

tasks.check { dependsOn("contractTest") }
