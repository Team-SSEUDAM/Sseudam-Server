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
    apply(plugin = libs.plugins.ktlint.get().pluginId)         // ktlint 플러그인 적용
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

    if (project.name == "core-api") {
        tasks.named<BootJar>("bootJar") {
            enabled = true
        }
        tasks.named<Jar>("jar") {
            enabled = false
        }

        apply(plugin = libs.plugins.docker.jib.get().pluginId)

        configure<JibExtension> {
            val imageTag: String   = System.getenv("META_TAGS") ?: "latest"
            val dockerUser: String = System.getenv("DOCKER_USER") ?: "sseudam"

            from {
                image = "amazoncorretto:21"
                platforms {
                    platform {
                        architecture = "arm64"
                        os           = "linux"
                    }
                }
            }
            to {
                image = "sseudam/sseudam-dev"
                tags  = setOf("latest", imageTag)
            }
            container {
                creationTime = "USE_CURRENT_TIMESTAMP"
                ports        = listOf("8080")
                user         = "1000:1000"
                jvmFlags     = listOf("-Duser.timezone=Asia/Seoul")
            }
        }
    } else {
        tasks.named<BootJar>("bootJar") {
            enabled = false
        }
        tasks.named<Jar>("jar") {
            enabled = true
        }

        tasks.matching { it.name.startsWith("jib") }.configureEach {
            enabled = false
        }
    }
}
