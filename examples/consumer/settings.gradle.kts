rootProject.name = "apoio-consumer-smoke"
if (providers.gradleProperty("useCheckout").orNull == "true") {
    includeBuild("../..")
}
dependencyResolutionManagement {
    repositories { maven { url = uri("../../build/test-maven") } }
}
