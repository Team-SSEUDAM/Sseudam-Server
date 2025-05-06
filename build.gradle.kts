plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.kotlin.kapt)
    alias(libs.plugins.kotlin.spring)
    alias(libs.plugins.kotlin.jpa)
    alias(libs.plugins.spring.boot)
    alias(libs.plugins.spring.dependency.management)
    alias(libs.plugins.ktlint)
}

ktlint {
    version.set(libs.versions.ktlint.version.set)
}

version = project.findProperty("applicationVersion")?.toString() ?: "0.1.0"
group = project.findProperty("projectGroup")?.toString() ?: "com.sseudam"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

repositories {
    mavenCentral()
}

extra["springModulithVersion"] = "1.3.5"

dependencies {
    // Spring
    implementation(libs.spring.boot.starter.web)
    implementation(libs.spring.boot.starter.data.jpa)
    implementation(libs.spring.boot.starter.actuator)
    implementation(libs.jakarta.validation)

    // Libraries
    implementation(libs.jackson.kotlin)
    implementation(libs.kotlin.reflect)

    // Spring Modulith (bundle 사용)
    implementation(libs.bundles.spring.modulith)
    runtimeOnly(libs.bundles.spring.modulith.runtime)

    // Database
    runtimeOnly(libs.postgresql.connector)
    runtimeOnly(libs.h2)

    // Test
    testImplementation(libs.spring.boot.starter.test)
    testImplementation(libs.kotlin.test.junit5)
    testRuntimeOnly(libs.junit.platform)

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

kotlin {
    compilerOptions {
        freeCompilerArgs.addAll("-Xjsr305=strict")
    }
}

tasks.register("addLintPreCommitHook", DefaultTask::class) {
    group = "setup"
    description = "Install git hooks"
    doLast {
        val hooksDir = project.file(".git/hooks")
        val scriptDir = project.file("scripts")
        val preCommit = scriptDir.resolve("pre-commit")
        Runtime.getRuntime().exec("chmod +x .git/hooks/pre-commit")
        preCommit.copyTo(hooksDir.resolve("pre-commit"), overwrite = true)
        hooksDir.resolve("pre-commit").setExecutable(true)
    }
}

tasks.named("compileKotlin") {
    dependsOn("addLintPreCommitHook")
}

allOpen {
    annotation("jakarta.persistence.Entity")
    annotation("jakarta.persistence.MappedSuperclass")
    annotation("jakarta.persistence.Embeddable")
}

tasks.withType<Test> {
    useJUnitPlatform()
}
