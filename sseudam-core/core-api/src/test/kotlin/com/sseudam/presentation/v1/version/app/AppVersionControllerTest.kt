package com.sseudam.presentation.v1.version.app

import com.sseudam.RestDocsTest
import com.sseudam.common.DeviceType
import com.sseudam.docs.RestDocsTestSuite
import com.sseudam.docs.support.DocsTag
import com.sseudam.docs.support.ENUM
import com.sseudam.docs.support.MockMvcExtensions.makeDocument
import com.sseudam.docs.support.ParamEnum
import com.sseudam.docs.support.RestDocsUtils.requestParameters
import com.sseudam.docs.support.RestDocsUtils.responseBody
import com.sseudam.docs.support.STRING
import com.sseudam.docs.support.parameterType
import com.sseudam.docs.support.type
import com.sseudam.fixture.version.app.AppVersionFixture
import com.sseudam.version.app.AppVersionService
import io.mockk.every
import io.mockk.mockk
import io.restassured.http.ContentType
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.http.HttpStatus
import org.springframework.restdocs.RestDocumentationContextProvider

@RestDocsTest
class AppVersionControllerTest : RestDocsTestSuite() {
    private lateinit var appVersionController: AppVersionController
    private lateinit var appVersionService: AppVersionService

    @BeforeEach
    fun setUpTest(restDocumentation: RestDocumentationContextProvider) {
        appVersionService = mockk()
        appVersionController = AppVersionController(appVersionService)
        mockMvcSpec = mockController(appVersionController)
    }

    @DisplayName("앱 버전 조회 - 200")
    @Test
    fun t1() {
        every { appVersionService.getAppVersion(DeviceType.IOS) } returns AppVersionFixture.iosVersion

        val response =
            given()
                .param("deviceType", DeviceType.IOS)
                .get("/api/v1/versions/app")
                .then()
                .status(HttpStatus.OK)
                .contentType(ContentType.JSON)

        response.makeDocument(
            "앱 버전 조회",
            DocsTag.VERSION,
            requestParameters =
                requestParameters(
                    "deviceType" parameterType ParamEnum(DeviceType::class) means "디바이스 타입" example "IOS",
                ),
            responseSchema = "AppVersionResponse",
            responseBody =
                responseBody(
                    "deviceType" type ENUM(DeviceType::class) means "앱 OS",
                    "currentVersion" type STRING means "앱 최신 버전",
                    "criticalVersion" type STRING means "강제 업데이트 대상 버전",
                ),
        )
    }
}
