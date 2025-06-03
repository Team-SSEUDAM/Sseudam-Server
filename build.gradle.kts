import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.jlleitschuh.gradle.ktlint.KtlintExtension
import com.google.cloud.tools.jib.gradle.JibExtension
import org.springframework.boot.gradle.tasks.bundling.BootJar

plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.kotlin.kapt)

    alias(libs.plugins.ktlint) apply false

    alias(libs.plugins.kotlin.spring) apply false
    alias(libs.plugins.spring.boot) apply false
    alias(libs.plugins.spring.dependency.management)

    alias(libs.plugins.kotlin.jpa) apply false
    alias(libs.plugins.asciidoctor.convert) apply false
    alias(libs.plugins.epages.restdocs.api.spec) apply false
    alias(libs.plugins.hidetake.swagger.generator) apply false

    alias(libs.plugins.docker.jib) apply false
}

allprojects {
    version = project.findProperty("applicationVersion")?.toString() ?: "0.1.0"
    group   = project.findProperty("projectGroup")?.toString() ?: "com.sseudam"
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

    configure<KtlintExtension> {
        version.set(libs.versions.ktlint.version.set)
        debug.set(false)
        verbose.set(false)
        android.set(false)
        outputToConsole.set(true)
        ignoreFailures.set(false)
    }

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

    tasks.withType<Test> {
        useJUnitPlatform()
    }

    tasks.named<BootJar>("bootJar") {
        enabled = true
    }
    tasks.named<org.gradle.jvm.tasks.Jar>("jar") {
        enabled = false
    }

    // ★ Jib 플러그드 적용
    apply(plugin = libs.plugins.docker.jib.get().pluginId)

    configure<JibExtension> {
        // 도커허브 사용자명과 이미지명은 CI/CD 환경변수로 넘겨받거나 기본값 설정
        val dockerUser: String = System.getenv("DOCKERHUB_USER") ?: "sseudam"
        val dockerImageName: String = System.getenv("DOCKERHUB_IMAGE_NAME") ?: "sseudam-dev"
        val imageShaTag: String = System.getenv("META_TAGS") ?: "$dockerUser/$dockerImageName:latest"

        // 태그만 추출 (예: abcd1234)
        val tag = imageShaTag.substringAfterLast(":")
        // Jib의 from (base image)
        from {
            image = "amazoncorretto:21"
            platforms {
                platform {
                    architecture = "arm64"
                    os           = "linux"
                }
            }
        }
        // Jib의 to (푸시할 레포지토리)
        to {
            image = "$dockerUser/$dockerImageName"
            tags  = setOf(tag)
        }
        container {
            creationTime = "USE_CURRENT_TIMESTAMP"
            ports        = listOf("8080")
            user         = "1000:1000"
            jvmFlags     = listOf("-Duser.timezone=Asia/Seoul")

            entrypoint = listOf("java", "-jar", "/workspace/libs/${project.name}-${project.version}.jar", "-Xmx1024m")
        }
    }
}
