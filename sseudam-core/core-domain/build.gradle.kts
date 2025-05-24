plugins {
    kotlin("kapt")
}

extra["springModulithVersion"] = "1.3.5"

dependencies {
    compileOnly(libs.spring.context)
    implementation(libs.spring.tx)
    implementation(libs.slf4j)
    implementation(libs.jakarta.annotation.api)

    // Coroutine
    implementation(libs.kotlinx.coroutine.core)
    implementation(libs.kotlinx.coroutine.reactor)
    implementation(libs.kotlinx.coroutine.slf4j)

    implementation(libs.reactor.kotlin)

    // Arrow Kt
    implementation(libs.arrow.fx.coroutine)
    implementation(libs.arrow.fx.stm)

    // Spring Modulith (bundle 사용)
    implementation(libs.bundles.spring.modulith)
    runtimeOnly(libs.bundles.spring.modulith.runtime)

    kapt("org.springframework.modulith:spring-modulith-docs:1.3.5")

    testImplementation(libs.spring.modulith.test)
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
