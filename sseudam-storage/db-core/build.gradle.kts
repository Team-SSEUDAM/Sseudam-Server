allOpen {
    annotation("jakarta.persistence.Entity")
    annotation("jakarta.persistence.MappedSuperclass")
    annotation("jakarta.persistence.Embeddable")
}

dependencies {
    api(libs.spring.boot.starter.data.jpa)
    implementation(libs.bundles.line.kotlin.jdsl)
    compileOnly(project(":sseudam-core:core-domain"))

    runtimeOnly(libs.postgresql.connector)
    runtimeOnly(libs.h2)

    testImplementation(project(":sseudam-core:core-domain"))
    testImplementation(project(":sseudam-tests:test-helper"))
    testImplementation(testFixtures(project(":sseudam-tests:test-container")))
}
