allOpen {
    annotation("jakarta.persistence.Entity")
    annotation("jakarta.persistence.MappedSuperclass")
    annotation("jakarta.persistence.Embeddable")
}

dependencies {
    api(libs.spring.boot.starter.data.jpa)
    implementation(libs.bundles.line.kotlin.jdsl)
    compileOnly(project(":sseudam-core:core-domain"))

    implementation(libs.flyway.core)
    implementation(libs.flyway.postgresql)

    // modulith
    implementation(libs.bundles.spring.modulith)
    runtimeOnly(libs.bundles.spring.modulith.runtime)

    runtimeOnly(libs.postgresql.connector)
    runtimeOnly(libs.h2)

    testImplementation(project(":sseudam-core:core-domain"))
    testImplementation(project(":sseudam-tests:test-helper"))
    testImplementation(testFixtures(project(":sseudam-tests:test-container")))
}

dependencyManagement {
    imports {
        mavenBom(
            libs.spring.modulith.bom
                .get()
                .toString(),
        )
    }
}
