tasks.getByName("bootJar") {
    enabled = true
}

tasks.getByName("jar") {
    enabled = false
}

dependencies {
    implementation(libs.spring.boot.starter.web)
    implementation(libs.spring.boot.starter.aop)
    implementation(libs.spring.boot.starter.validation)
    implementation(libs.jakarta.validation)

    // Security
    implementation(libs.spring.boot.starter.security)
    testImplementation(libs.spring.security.test)
    implementation(libs.jjwt.api)
    runtimeOnly(libs.jjwt.jackson)
    runtimeOnly(libs.jjwt.impl)

    implementation(project(":sseudam-admin"))
    implementation(project(":sseudam-core:core-domain"))
    implementation(project(":sseudam-clients:notification"))
    implementation(project(":sseudam-clients:oauth-client"))
    implementation(project(":sseudam-clients:aws"))
    implementation(project(":sseudam-supports:swagger"))

    runtimeOnly(project(":sseudam-supports:logging"))
    runtimeOnly(project(":sseudam-supports:monitoring"))
    runtimeOnly(project(":sseudam-storage:db-core"))
    runtimeOnly(project(":sseudam-storage:redis"))

    testImplementation(libs.spring.boot.starter.test)
    testImplementation(project(":sseudam-storage:db-core"))
    testImplementation(project(":sseudam-storage:redis"))
    testImplementation(project(":sseudam-tests:test-helper"))
    testImplementation(testFixtures(project(":sseudam-tests:test-container")))
}
