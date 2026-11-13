dependencies {
    api(project(":core"))
    // Compile-only Android API signatures; no SDK or Android classes are bundled.
    compileOnly("com.google.android:android:4.1.1.4") { isTransitive = false }
    testImplementation("com.google.android:android:4.1.1.4") { isTransitive = false }
    testImplementation("junit:junit:4.13.2")
    testImplementation("org.mockito:mockito-core:5.14.2")
    testRuntimeOnly("xmlpull:xmlpull:1.1.3.1")
}
