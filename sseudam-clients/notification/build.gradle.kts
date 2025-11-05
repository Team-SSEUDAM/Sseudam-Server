dependencies {
    // Notification
    implementation(libs.bundles.openfeign)
    implementation(libs.firebase)
    implementation(libs.spring.retry)
    implementation(libs.spring.boot.starter.aspectj)

    implementation(project(":sseudam-core:core-application"))
    implementation(project(":sseudam-core:core-contract"))

    testImplementation(project(":sseudam-tests:test-helper"))
}
