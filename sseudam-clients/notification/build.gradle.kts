dependencies {
    // Notification
    implementation(libs.bundles.openfeign)
    implementation(libs.firebase)
    implementation(libs.spring.retry)
    implementation(libs.spring.boot.starter.aop)

    implementation(project(":sseudam-core:core-domain"))
    implementation(project(":sseudam-core:core-contract"))

    testImplementation(project(":sseudam-tests:test-helper"))
}
