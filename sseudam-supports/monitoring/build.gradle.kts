dependencies {
    implementation(libs.spring.boot.starter.actuator)
    implementation(libs.micrometer.registry.prometheus)

    // modulith
    implementation(libs.bundles.spring.modulith)
    runtimeOnly(libs.bundles.spring.modulith.runtime)
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
