// build.gradle.kts

import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.jlleitschuh.gradle.ktlint.KtlintExtension

plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.kotlin.kapt)
    alias(libs.plugins.kotlin.spring) apply false
    alias(libs.plugins.docker.jib)             // ← Jib 플러그인 적용
    alias(libs.plugins.ktlint) apply false
    alias(libs.plugins.spring.boot) apply false
    alias(libs.plugins.spring.dependency.management)
    alias(libs.plugins.kotlin.jpa) apply false
    alias(libs.plugins.asciidoctor.convert) apply false
    alias(libs.plugins.epages.restdocs.api.spec) apply false
    alias(libs.plugins.hidetake.swagger.generator) apply false
}

allprojects {
    version = project.findProperty("applicationVersion")?.toString() ?: "0.1.0"
    group = project.findProperty("projectGroup")?.toString() ?: "com.sseudam"
}

subprojects {
    val libs = rootProject.libs

    apply(plugin = libs.plugins.kotlin.jvm.get().pluginId)
    apply(plugin = libs.plugins.kotlin.kapt.get().pluginId)
    apply(plugin = libs.plugins.kotlin.spring.get().pluginId)
    apply(plugin = libs.plugins.ktlint.get().pluginId)
    apply(plugin = libs.plugins.kotlin.jpa.get().pluginId)
    apply(plugin = libs.plugins.spring.boot.get().pluginId)
    apply(plugin = libs.plugins.spring.dependency.management.get().pluginId)
    apply(plugin = libs.plugins.asciidoctor.convert.get().pluginId)
    apply(plugin = libs.plugins.epages.restdocs.api.spec.get().pluginId)
    apply(plugin = libs.plugins.hidetake.swagger.generator.get().pluginId)

    java {
        sourceCompatibility = JavaVersion.VERSION_21
    }

    dependencies {
        implementation(libs.kotlin.reflect)
        implementation(libs.kotlin.stdlib.jdk8)
        implementation(libs.jackson.kotlin)
        implementation(libs.hibernate.spatial)

        annotationProcessor(libs.spring.boot.configuration.processor)
        kapt(libs.spring.boot.configuration.processor)

        testImplementation(libs.spring.mockk)
        testImplementation(libs.bundles.kotest)
        testImplementation(libs.spring.boot.starter.test)
        testImplementation(libs.spring.security.test)
    }

    tasks.withType<KotlinCompile> {
        kotlin {
            compilerOptions {
                freeCompilerArgs.set(listOf("-Xjsr305=strict"))
                jvmTarget.set(JvmTarget.JVM_21)
            }
        }
    }

    // ktlint 설정 추가
    configure<KtlintExtension> {
        version.set(libs.versions.ktlint.version.set)
    }

    // ─── Spring Boot 빌드 설정 ──────────────────────────────────────────────────
    // Jib가 Spring Boot의 fat JAR을 올바르게 참조하려면 bootJar를 켜고, plain jar는 비활성화해야 한다.
    tasks.named("bootJar") {
        enabled = true
    }
    tasks.named("jar") {
        enabled = false
    }
    // ────────────────────────────────────────────────────────────────────────────

    tasks.withType<Test> {
        useJUnitPlatform()
    }
}

// ─── Jib 플러그인 설정 ────────────────────────────────────────────────────────
jib {
    val imageTag: String = System.getenv("META_TAGS") ?: "latest"
    val dockerUser: String = System.getenv("DOCKER_USER") ?: "sseudam"

    from {
        image = "amazoncorretto:21"      // 기존 Dockerfile과 동일한 베이스 이미지
        platforms {
            platform {
                architecture = "arm64"
                os = "linux"
            }
        }
    }
    to {
        image = "$dockerUser/$imageTag"
        tags = setOf("latest", imageTag)
    }
    container {
        creationTime = "USE_CURRENT_TIMESTAMP"
        ports = listOf("8080")
        user = "1000:1000"
        jvmFlags = listOf("-Duser.timezone=Asia/Seoul")
    }
}
// ────────────────────────────────────────────────────────────────────────────

// Git hooks 설치 태스크
tasks.register("addLintPreCommitHook", DefaultTask::class) {
    group = "setup"
    description = "Install git hooks"
    doLast {
        val hooksDir = project.file(".git/hooks")
        val sourceDir = project.file(".githooks")
        val preCommit = sourceDir.resolve("pre-commit")
        Runtime.getRuntime().exec("chmod +x .git/hooks/pre-commit")
        preCommit.copyTo(hooksDir.resolve("pre-commit"), overwrite = true)
        hooksDir.resolve("pre-commit").setExecutable(true)
    }
}

tasks.named("compileKotlin") {
    dependsOn("addLintPreCommitHook")
}
