plugins { application }
val libraryVersion = file("../../gradle.properties").readLines()
    .first { it.startsWith("VERSION_NAME=") }.substringAfter("=").trim()
dependencies {
    // Intentionally consume android only: core must arrive transitively from its POM.
    implementation("dev.brenopoubel.apoio:android:$libraryVersion")
}
application { mainClass.set("Smoke") }
tasks.withType<JavaCompile>().configureEach { options.release.set(8) }
