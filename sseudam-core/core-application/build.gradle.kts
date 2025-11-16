
dependencies {
    api(libs.kotlin.logging)
    compileOnly(libs.spring.context)
    api(project(":sseudam-core:core-contract"))
    api(project(":sseudam-core:core-domain"))

    // Coroutine
    implementation(libs.kotlinx.coroutine.core)
    implementation(libs.kotlinx.coroutine.reactor)
    implementation(libs.kotlinx.coroutine.slf4j)
    implementation(libs.reactor.kotlin)
    // Arrow Kt
    implementation(libs.arrow.fx.coroutine)
    implementation(libs.arrow.fx.stm)

    implementation(libs.spring.tx)
    implementation(libs.slf4j)

    // modulith
    implementation(libs.bundles.spring.modulith)
    runtimeOnly(libs.bundles.spring.modulith.runtime)

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
