package com.sseudam.presentation.v1.user

import com.sseudam.RestDocsTest
import com.sseudam.config.UserArgumentResolver
import com.sseudam.docs.RestDocsTestSuite
import com.sseudam.docs.support.Authorization
import com.sseudam.docs.support.BOOLEAN
import com.sseudam.docs.support.DocsTag
import com.sseudam.docs.support.MockMvcExtensions.makeDocument
import com.sseudam.docs.support.NUMBER
import com.sseudam.docs.support.RestDocsUtils.headers
import com.sseudam.docs.support.RestDocsUtils.requestBody
import com.sseudam.docs.support.RestDocsUtils.responseBody
import com.sseudam.docs.support.STRING
import com.sseudam.docs.support.headerType
import com.sseudam.docs.support.type
import com.sseudam.fixture.user.UserFixture
import com.sseudam.presentation.v1.user.request.NicknameRequest
import com.sseudam.presentation.v1.user.request.UserMobileDeviceRequest
import com.sseudam.user.User
import com.sseudam.user.UserDeviceService
import com.sseudam.user.UserFacade
import com.sseudam.user.UserService
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.restassured.http.ContentType
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import java.util.UUID

@RestDocsTest
class UserControllerTest : RestDocsTestSuite() {
    private lateinit var userController: UserController
    private lateinit var userService: UserService
    private lateinit var userFacade: UserFacade
    private lateinit var userDeviceService: UserDeviceService
    private lateinit var userArgumentResolver: UserArgumentResolver

    @BeforeEach
    fun setUpTest() {
        userArgumentResolver = mockk()
        userService = mockk()
        userFacade = mockk()
        userDeviceService = mockk()
        userController =
            UserController(
                userService = userService,
                userFacade = userFacade,
                userDeviceService = userDeviceService,
            )
        mockMvcSpec = mockController(userController, userArgumentResolver)
        every { userArgumentResolver.supportsParameter(any()) } returns true
        every { userArgumentResolver.resolveArgument(any(), any(), any(), any()) } returns
            User(
                id = 1L,
                key = UUID.randomUUID().toString(),
            )
    }

    @DisplayName("내 정보 조회 - 200")
    @Test
    fun t1() {
        every { userService.getProfile(any()) } returns UserFixture.userProfile

        val response =
            given()
                .header(HttpHeaders.AUTHORIZATION, "Bearer accessToken")
                .contentType(ContentType.JSON)
                .get("/api/v1/users/me")
                .then()
                .status(HttpStatus.OK)

        response.makeDocument(
            "내 정보 조회",
            DocsTag.USER,
            headers(
                "Authorization" headerType Authorization,
            ),
            "UserProfileResponse",
            responseBody(
                "userId" type NUMBER means "사용자 ID",
                "email" type STRING means "이메일",
                "name" type STRING means "이름",
                "nickname" type STRING means "닉네임",
            ),
        )
    }

    @DisplayName("회원탈퇴 - 200")
    @Test
    fun t2() {
        every { userFacade.withdrawalUser(any()) } returns Unit

        val response =
            given()
                .header(HttpHeaders.AUTHORIZATION, "Bearer accessToken")
                .delete("/api/v1/users")
                .then()
                .status(HttpStatus.OK)
                .contentType(ContentType.JSON)

        response.makeDocument(
            "회원탈퇴",
            DocsTag.USER,
            headers(
                "Authorization" headerType Authorization,
            ),
            "UserWithdrawalResponse",
            responseBody(
                "message" type STRING means "탈퇴 완료 메시지",
            ),
        )
    }

    @DisplayName("닉네임 수정 - 200")
    @Test
    fun t3() {
        val request = NicknameRequest("newNickname")

        every { userService.updateNickname(any(), any()) } returns UserFixture.userProfile

        val response =
            given()
                .header(HttpHeaders.AUTHORIZATION, "Bearer accessToken")
                .contentType(ContentType.JSON)
                .body(request)
                .put("/api/v1/users/nickname")
                .then()
                .status(HttpStatus.OK)
                .contentType(ContentType.JSON)

        response.makeDocument(
            "닉네임 수정",
            DocsTag.USER,
            headers(
                "Authorization" headerType Authorization,
            ),
            "NicknameRequest",
            "UserProfileResponse",
            requestBody(
                "nickname" type STRING means "새로운 닉네임" example "newNickname",
            ),
            responseBody(
                "userId" type NUMBER means "사용자 ID",
                "email" type STRING means "이메일",
                "name" type STRING means "이름",
                "nickname" type STRING means "수정된 닉네임",
            ),
        )
    }

    @DisplayName("닉네임 유효성 검사 - 200")
    @Test
    fun t4() {
        val request = NicknameRequest("validNickname")

        every { userService.validateNickname(any()) } returns true

        val response =
            given()
                .header(HttpHeaders.AUTHORIZATION, "Bearer accessToken")
                .contentType(ContentType.JSON)
                .body(request)
                .post("/api/v1/users/nickname/validate")
                .then()
                .status(HttpStatus.OK)
                .contentType(ContentType.JSON)

        response.makeDocument(
            "닉네임 유효성 검사",
            DocsTag.USER,
            headers(
                "Authorization" headerType Authorization,
            ),
            "NicknameRequest",
            "IsValidateNicknameResponse",
            requestBody(
                "nickname" type STRING means "검증할 닉네임" example "validNickname",
            ),
            responseBody(
                "isValid" type BOOLEAN means "유효성 검사 결과",
                "message" type STRING means "검사 결과 메시지",
            ),
        )
    }

    @DisplayName("FCM 토큰 추가 및 갱신 - 200")
    @Test
    fun t5() {
        val request = UserMobileDeviceRequest("fcmToken123")

        every { userDeviceService.append(any()) } just Runs

        val response =
            given()
                .header(HttpHeaders.AUTHORIZATION, "Bearer accessToken")
                .header("X-DEVICE-ID", "device123")
                .contentType(ContentType.JSON)
                .body(request)
                .put("/api/v1/users/fcm-token")
                .then()
                .status(HttpStatus.OK)
                .contentType(ContentType.JSON)

        response.makeDocument(
            "FCM 토큰 추가",
            DocsTag.USER,
            headers(
                "Authorization" headerType Authorization,
            ),
            "UserMobileDeviceRequest",
            "UserMobileDeviceResponse",
            requestBody(
                "fcmToken" type STRING means "FCM 토큰" example "fcmToken123",
            ),
            responseBody(
                "message" type STRING means "등록 완료 메시지",
            ),
        )
    }
}
