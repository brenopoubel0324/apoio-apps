plugins { base }

allprojects {
    group = "dev.brenopoubel.apoio"
    version = providers.gradleProperty("VERSION_NAME").get()
    repositories { mavenCentral() }
}

subprojects {
    apply(plugin = "java-library")
    apply(plugin = "maven-publish")

    configure<JavaPluginExtension> {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
        withSourcesJar()
    }
    tasks.withType<JavaCompile>().configureEach {
        options.release.set(8)
        options.encoding = "UTF-8"
    }
    tasks.withType<Jar>().configureEach {
        isPreserveFileTimestamps = false
        isReproducibleFileOrder = true
    }
    configure<PublishingExtension> {
        publications {
            register<MavenPublication>("library") {
                from(components["java"])
                pom {
                    name.set("Apoio Apps - ${project.name}")
                    description.set("Apoio voluntario reutilizavel para os apps de Breno Poubel")
                    url.set("https://github.com/brenopoubel0324/apoio-apps")
                    scm { url.set("https://github.com/brenopoubel0324/apoio-apps") }
                }
            }
        }
        repositories {
            maven {
                name = "LocalTest"
                url = rootProject.layout.buildDirectory.dir("test-maven").get().asFile.toURI()
            }
            maven {
                name = "GitHubPackages"
                url = uri("https://maven.pkg.github.com/brenopoubel0324/apoio-apps")
                credentials {
                    username = providers.gradleProperty("gpr.user").orElse(providers.environmentVariable("GITHUB_ACTOR")).orNull
                    password = providers.gradleProperty("gpr.key").orElse(providers.environmentVariable("GITHUB_TOKEN")).orNull
                }
            }
        }
    }
}

tasks.named("check") { dependsOn(":core:check", ":android:check") }
tasks.named("assemble") { dependsOn(":core:assemble", ":android:assemble") }
