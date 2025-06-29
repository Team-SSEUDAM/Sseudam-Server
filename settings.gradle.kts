pluginManagement {
    repositories {
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    @Suppress("UnstableApiUsage")
    repositories {
        mavenCentral()
        gradlePluginPortal()
    }
}


rootProject.name = "sseudam"

include(
    "sseudam-admin"
)

include(
    "sseudam-core:core-api",
    "sseudam-core:core-domain",
)

include(
    "sseudam-storage:db-core",
    "sseudam-storage:redis",
)

include(
    "sseudam-batch"
)

include(
    "sseudam-clients:aws",
    "sseudam-clients:notification",
    "sseudam-clients:oauth-client",
)

include(
    "sseudam-supports:logging",
    "sseudam-supports:monitoring",
    "sseudam-supports:swagger",
)

include(
    "sseudam-tests:test-helper",
    "sseudam-tests:test-container",
)

