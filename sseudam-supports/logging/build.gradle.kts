dependencies {
    implementation(libs.sentry.logback)
    runtimeOnly(libs.micrometer.tracing.bridge.brave)
}
