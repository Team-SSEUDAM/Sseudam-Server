
dependencies {
    compileOnly(libs.spring.context)
    implementation(libs.spring.tx)
    implementation(libs.slf4j)
    implementation(libs.jakarta.annotation.api)
    implementation(libs.reactor.kotlin)

    // Coroutine
    implementation(libs.kotlinx.coroutine.core)
    implementation(libs.kotlinx.coroutine.reactor)
    implementation(libs.kotlinx.coroutine.slf4j)

    // modulith
    implementation(libs.bundles.spring.modulith)
    runtimeOnly(libs.bundles.spring.modulith.runtime)

    // Arrow Kt
    implementation(libs.arrow.fx.coroutine)
    implementation(libs.arrow.fx.stm)

    testImplementation(libs.bundles.spring.modulith.tests)
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
