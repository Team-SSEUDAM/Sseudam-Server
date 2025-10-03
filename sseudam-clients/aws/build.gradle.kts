dependencies {
    compileOnly(libs.spring.boot.starter.web)

    implementation(libs.bundles.aws.client)
    implementation(project(":sseudam-core:core-application"))

    testImplementation(libs.spring.boot.starter.web)
    testImplementation(testFixtures(project(":sseudam-tests:test-container")))
}
