dependencies {
    api(libs.spring.boot.starter.oauth2.resource.server)
    implementation(libs.bouncycastle.bcpkix)
    implementation(libs.bundles.openfeign)

    implementation(project(":sseudam-core:core-domain"))
}
