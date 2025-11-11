package com.sseudam.docs

import io.restassured.module.mockmvc.RestAssuredMockMvc
import io.restassured.module.mockmvc.specification.MockMvcRequestSpecification
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.restdocs.RestDocumentationContextProvider
import org.springframework.restdocs.RestDocumentationExtension
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.test.web.servlet.setup.StandaloneMockMvcBuilder
import org.springframework.web.filter.CharacterEncodingFilter
import org.springframework.web.method.support.HandlerMethodArgumentResolver

@Tag("restdocs")
@ExtendWith(RestDocumentationExtension::class)
abstract class RestDocsTestSuite {
    lateinit var mockMvcSpec: MockMvcRequestSpecification
    private lateinit var restDocumentation: RestDocumentationContextProvider

    @BeforeEach
    fun setUp(restDocumentation: RestDocumentationContextProvider) {
        this.restDocumentation = restDocumentation
    }

    protected fun given(): MockMvcRequestSpecification = mockMvcSpec

    protected fun mockController(controller: Any): MockMvcRequestSpecification {
        val mockMvc = createMockMvc(controller)
        return RestAssuredMockMvc.given().mockMvc(mockMvc)
    }

    protected fun mockController(
        controller: Any,
        argumentResolver: HandlerMethodArgumentResolver,
    ): MockMvcRequestSpecification {
        val mockMvc = createMockMvc(controller, argumentResolver)
        return RestAssuredMockMvc.given().mockMvc(mockMvc)
    }

    private fun createMockMvc(controller: Any): MockMvc =
        MockMvcBuilders
            .standaloneSetup(controller)
            .addFilter<StandaloneMockMvcBuilder>(CharacterEncodingFilter("UTF-8", true))
            .apply<StandaloneMockMvcBuilder>(MockMvcRestDocumentation.documentationConfiguration(restDocumentation))
            .build()

    private fun createMockMvc(
        controller: Any,
        argumentResolver: HandlerMethodArgumentResolver,
    ): MockMvc =
        MockMvcBuilders
            .standaloneSetup(controller)
            .addFilter<StandaloneMockMvcBuilder>(CharacterEncodingFilter("UTF-8", true))
            .apply<StandaloneMockMvcBuilder>(MockMvcRestDocumentation.documentationConfiguration(restDocumentation))
            .setCustomArgumentResolvers(argumentResolver)
            .build()
}
