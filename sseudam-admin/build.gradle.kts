dependencies {
    implementation(libs.spring.boot.starter.web)
    implementation(libs.spring.boot.starter.aop)
    implementation(libs.spring.boot.starter.validation)
    implementation(libs.jakarta.validation)

    implementation(libs.spring.boot.starter.security)
    testImplementation(libs.spring.security.test)
    implementation(libs.jjwt.api)
    runtimeOnly(libs.jjwt.jackson)
    runtimeOnly(libs.jjwt.impl)

    implementation(project(":sseudam-core:core-domain"))
    implementation(project(":sseudam-storage:db-core"))
    implementation(project(":sseudam-storage:redis"))
    implementation(project(":sseudam-clients:notification"))
    implementation(project(":sseudam-clients:aws"))
    implementation(project(":sseudam-supports:swagger"))
}
