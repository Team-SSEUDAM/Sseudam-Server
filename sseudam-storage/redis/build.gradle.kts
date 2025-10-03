dependencies {
    api(libs.spring.boot.starter.redis)

    implementation(libs.redisson)
    implementation(project(":sseudam-core:core-application"))
    implementation(project(":sseudam-core:core-contract"))
}
