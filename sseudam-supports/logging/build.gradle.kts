dependencies {
    implementation(libs.sentry.logback)
    runtimeOnly(libs.micrometer.tracing.bridge.brave)
    implementation(libs.spring.opentelemetry)
}
