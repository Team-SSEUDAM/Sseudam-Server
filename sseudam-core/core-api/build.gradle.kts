import com.google.cloud.tools.jib.gradle.JibExtension

plugins {
    alias(libs.plugins.jib)
}

tasks.getByName("bootJar") {
    enabled = true
}

tasks.getByName("jar") {
    enabled = false
}

jib {
    from {
        image = "amazoncorretto:21-alpine"
        platforms {
            platform {
                architecture = "amd64"
                os = "linux"
            }
            platform {
                architecture = "arm64"
                os = "linux"
            }
        }
    }
    to {
        image = "sseudam/sseudam-server"
    }
    container {
        jvmFlags =
            listOf(
                "-Xmx1024m",
                "-Xms512m",
                "-XX:+UseG1GC",
                "-XX:+UseContainerSupport",
                "-XX:MaxRAMPercentage=75.0",
                "-Dfile.encoding=UTF-8",
                "-Duser.timezone=Asia/Seoul",
                "-Djava.security.egd=file:/dev/./urandom",
            )
        ports = listOf("8080")
        environment =
            mapOf(
                "TZ" to "Asia/Seoul",
            )
        creationTime = "USE_CURRENT_TIMESTAMP"
        user = "1000:1000"
    }

    // 압축 최적화
    containerizingMode = "packaged"

    // 캐시 설정
    outputPaths {
        tar =
            layout.buildDirectory
                .file("jib-image.tar")
                .get()
                .asFile.absolutePath
        digest =
            layout.buildDirectory
                .file("jib-image.digest")
                .get()
                .asFile.absolutePath
        imageId =
            layout.buildDirectory
                .file("jib-image.id")
                .get()
                .asFile.absolutePath
    }
}

tasks.register("jibDev") {
    group = "jib"
    description = "Build and push dev image"
    doLast {
        project.extensions.configure<JibExtension> {
            to {
                image = "sseudam/sseudam-dev"
                auth {
                    username = System.getProperty("jib.to.auth.username") ?: "sseudam"
                    password = System.getProperty("jib.to.auth.password") ?: System.getenv("DOCKERHUB_ACCESS_TOKEN")
                }
            }
            container {
                environment =
                    mapOf(
                        "TZ" to "Asia/Seoul",
                        "SPRING_PROFILES_ACTIVE" to "dev",
                    )
            }
        }
    }
    finalizedBy("jib")
}

tasks.register("jibProd") {
    group = "jib"
    description = "Build and push prod image"
    doLast {
        project.extensions.configure<JibExtension> {
            to {
                image = "sseudam/sseudam-prod"
                auth {
                    username = System.getProperty("jib.to.auth.username") ?: "sseudam"
                    password = System.getProperty("jib.to.auth.password") ?: System.getenv("DOCKERHUB_ACCESS_TOKEN")
                }
            }
            container {
                environment =
                    mapOf(
                        "TZ" to "Asia/Seoul",
                        "SPRING_PROFILES_ACTIVE" to "prod",
                    )
            }
        }
    }
    finalizedBy("jib")
}

dependencies {
    implementation(libs.spring.boot.starter.web)
    implementation(libs.spring.boot.starter.aop)
    implementation(libs.spring.boot.starter.validation)
    implementation(libs.jakarta.validation)

    // modulith
    implementation(libs.bundles.spring.modulith)
    runtimeOnly(libs.bundles.spring.modulith.runtime)

    // Security
    implementation(libs.spring.boot.starter.security)
    testImplementation(libs.spring.security.test)
    implementation(libs.jjwt.api)
    runtimeOnly(libs.jjwt.jackson)
    runtimeOnly(libs.jjwt.impl)

    implementation(project(":sseudam-admin"))
    implementation(project(":sseudam-batch"))
    implementation(project(":sseudam-core:core-domain"))
    implementation(project(":sseudam-clients:notification"))
    implementation(project(":sseudam-clients:oauth-client"))
    implementation(project(":sseudam-clients:aws"))
    implementation(project(":sseudam-supports:swagger"))

    runtimeOnly(project(":sseudam-supports:logging"))
    runtimeOnly(project(":sseudam-supports:monitoring"))
    runtimeOnly(project(":sseudam-storage:db-core"))
    runtimeOnly(project(":sseudam-storage:redis"))

    testImplementation(libs.spring.boot.starter.test)
    testImplementation(project(":sseudam-storage:db-core"))
    testImplementation(project(":sseudam-storage:redis"))
    testImplementation(project(":sseudam-tests:api-docs"))
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
