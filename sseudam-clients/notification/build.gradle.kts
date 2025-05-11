dependencies {
    // Notification
    implementation(libs.bundles.openfeign)
    implementation(libs.firebase)

    implementation(project(":sseudam-core:core-domain"))

    testImplementation(project(":sseudam-tests:test-helper"))
}
