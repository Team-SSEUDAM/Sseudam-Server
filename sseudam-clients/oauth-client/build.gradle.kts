dependencies {
    api(libs.spring.boot.starter.oauth2.resource.server)
    implementation(libs.bundles.openfeign)

    implementation(project(":sseudam-core:core-domain"))
}
