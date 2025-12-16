dependencies {
    compileOnly(libs.jakarta.servlet.api)
    compileOnly(libs.spring.boot.starter.test)
    api(libs.bundles.spring.restdocs)
    api(libs.restassured.spring.mock.mvc)
    api(libs.epages.restdocs.api.spec.mock.mvc)
    // Jackson 3.0에서 jackson-datatype-jsr310은 jackson-databind에 통합됨
    api(libs.bundles.kotest)
    api(libs.kotest.runner.junit5)
    api(libs.kotest.extensions)
    api(libs.spring.boot.starter.security)
    api(libs.spring.security.test)
}
