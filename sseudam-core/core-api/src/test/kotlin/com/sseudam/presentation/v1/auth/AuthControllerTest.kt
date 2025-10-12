package com.sseudam.presentation.v1.auth

import com.sseudam.RestDocsTest
import com.sseudam.auth.AuthenticationFacade
import com.sseudam.auth.AuthenticationService
import com.sseudam.auth.Token
import com.sseudam.auth.result.SocialLoginResult
import com.sseudam.client.oauth.AppleClientResult
import com.sseudam.client.oauth.KaKaoClientResult
import com.sseudam.client.oauth.OAuthService
import com.sseudam.docs.RestDocsTestSuite
import com.sseudam.docs.support.BOOLEAN
import com.sseudam.docs.support.DocsTag
import com.sseudam.docs.support.MockMvcExtensions.makeDocument
import com.sseudam.docs.support.RestDocsUtils.requestBody
import com.sseudam.docs.support.RestDocsUtils.responseBody
import com.sseudam.docs.support.STRING
import com.sseudam.docs.support.type
import com.sseudam.presentation.v1.auth.request.LoginRequest
import com.sseudam.presentation.v1.auth.request.RefreshTokenRequest
import com.sseudam.presentation.v1.auth.request.SignUpRequest
import com.sseudam.presentation.v1.auth.request.SignUpSocialRequest
import com.sseudam.presentation.v1.auth.request.TokenRequest
import com.sseudam.user.SocialType
import com.sseudam.user.SocialUser
import com.sseudam.user.UserCredentials
import com.sseudam.user.UserService
import io.mockk.every
import io.mockk.mockk
import io.restassured.http.ContentType
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.http.HttpStatus
import org.springframework.security.crypto.password.PasswordEncoder

@RestDocsTest
class AuthControllerTest : RestDocsTestSuite() {
    private lateinit var authController: AuthController
    private lateinit var passwordEncoder: PasswordEncoder
    private lateinit var authenticationService: AuthenticationService
    private lateinit var authenticationFacade: AuthenticationFacade
    private lateinit var oAuthService: OAuthService
    private lateinit var userService: UserService

    @BeforeEach
    fun setUpTest() {
        passwordEncoder = mockk()
        authenticationService = mockk()
        authenticationFacade = mockk()
        oAuthService = mockk()
        userService = mockk()
        authController =
            AuthController(
                passwordEncoder = passwordEncoder,
                authenticationService = authenticationService,
                authenticationFacade = authenticationFacade,
                oAuthService = oAuthService,
                userService = userService,
            )
        mockMvcSpec = mockController(authController)
    }

    @DisplayName("로그인 - 200")
    @Test
    fun t1() {
        val userCredential = UserCredentials(1L, "userKey", "test@test.com", "{noop}password")
        val token = Token("accessToken", "refreshToken")

        every { userService.getUserCredential(any()) } returns userCredential
        every { passwordEncoder.matches(any(), any()) } returns true
        every { authenticationService.login(any(), any(), any()) } returns token

        val request = LoginRequest("test@test.com", "password")

        val response =
            given()
                .header("X-DEVICE-ID", "device123")
                .contentType(ContentType.JSON)
                .body(request)
                .post("/api/v1/auth/test-login")
                .then()
                .status(HttpStatus.OK)
                .contentType(ContentType.JSON)

        response.makeDocument(
            "로그인",
            DocsTag.AUTH,
            requestBody =
                requestBody(
                    "loginId" type STRING means "로그인 아이디" example "test@test.com",
                    "password" type STRING means "비밀번호" example "password",
                ),
            responseSchema = "TokenResponse",
            responseBody =
                responseBody(
                    "isTemporaryToken" type BOOLEAN means "임시 토큰 여부",
                    "accessToken" type STRING means "액세스 토큰",
                    "refreshToken" type STRING means "리프레시 토큰",
                ),
        )
    }

    @DisplayName("회원가입 - 200")
    @Test
    fun t2() {
        every { passwordEncoder.encode(any()) } returns "encodedPassword"
        every { userService.create(any()) } returns mockk()

        val request = SignUpRequest("test@test.com", "password", "윤범차", "닉네임이야")

        val response =
            given()
                .contentType(ContentType.JSON)
                .body(request)
                .post("/api/v1/auth/signup")
                .then()
                .status(HttpStatus.OK)
                .contentType(ContentType.JSON)

        response.makeDocument(
            "회원가입",
            DocsTag.AUTH,
            requestBody =
                requestBody(
                    "email" type STRING means "이메일" example "test@test.com",
                    "password" type STRING means "비밀번호" example "password",
                    "name" type STRING means "이름" example "윤범차",
                    "nickname" type STRING means "닉네임" example "닉네임이야",
                ),
            responseSchema = "SignUpResponse",
            responseBody =
                responseBody(
                    "message" type STRING means "회원가입 메시지",
                ),
        )
    }

    @DisplayName("카카오 소셜 로그인 - 200")
    @Test
    fun t3() {
        val socialInfo = KaKaoClientResult("kakaoId123", "test@kakao.com", "카카오유저", "카카오닉네임")
        val token = Token("accessToken", "refreshToken")

        every { oAuthService.getKaKaoUserInfo(any()) } returns socialInfo
        every { authenticationFacade.socialLogin(any(), any()) } returns SocialLoginResult(token, false)

        val request = TokenRequest("kakao_access_token")

        val response =
            given()
                .contentType(ContentType.JSON)
                .body(request)
                .post("/api/v1/auth/social-login/kakao")
                .then()
                .status(HttpStatus.OK)
                .contentType(ContentType.JSON)

        response.makeDocument(
            "카카오 소셜 로그인",
            DocsTag.AUTH,
            requestBody =
                requestBody(
                    "token" type STRING means "카카오 액세스 토큰" example "kakao_access_token",
                ),
            responseSchema = "TokenResponse",
            responseBody =
                responseBody(
                    "isTemporaryToken" type BOOLEAN means "임시 토큰 여부",
                    "accessToken" type STRING means "액세스 토큰",
                    "refreshToken" type STRING means "리프레시 토큰",
                ),
        )
    }

    @DisplayName("애플 소셜 로그인 - 200")
    @Test
    fun t4() {
        val socialInfo = AppleClientResult("appleId123", "test@apple.com")
        val token = Token("accessToken", "refreshToken")

        every { oAuthService.getAppleUserInfo(any()) } returns socialInfo
        every { authenticationFacade.socialLogin(any(), any()) } returns SocialLoginResult(token, false)

        val request = TokenRequest("apple_id_token")

        val response =
            given()
                .contentType(ContentType.JSON)
                .body(request)
                .post("/api/v1/auth/social-login/apple")
                .then()
                .status(HttpStatus.OK)
                .contentType(ContentType.JSON)

        response.makeDocument(
            "애플 소셜 로그인",
            DocsTag.AUTH,
            requestBody =
                requestBody(
                    "token" type STRING means "애플 ID 토큰" example "apple_id_token",
                ),
            responseSchema = "TokenResponse",
            responseBody =
                responseBody(
                    "isTemporaryToken" type BOOLEAN means "임시 토큰 여부",
                    "accessToken" type STRING means "액세스 토큰",
                    "refreshToken" type STRING means "리프레시 토큰",
                ),
        )
    }

    @DisplayName("소셜 회원가입 - 200")
    @Test
    fun t5() {
        val tempUser = SocialUser(1L, "userKey", "카카오유저", "kakaoId123", SocialType.KAKAO)

        every { userService.getSocialUserByEmail(any()) } returns tempUser
        every { userService.socialSignUp(any(), any()) } returns Unit

        val request = SignUpSocialRequest("test@kakao.com", "쓰담", "서울시 영등포구 여의도동 123-45")

        val response =
            given()
                .contentType(ContentType.JSON)
                .body(request)
                .post("/api/v1/auth/social-signup")
                .then()
                .status(HttpStatus.OK)
                .contentType(ContentType.JSON)

        response.makeDocument(
            "소셜 회원가입",
            DocsTag.AUTH,
            requestBody =
                requestBody(
                    "email" type STRING means "소셜 이메일" example "test@kakao.com",
                    "name" type STRING means "사용자 닉네임" example "쓰담",
                    "address" type STRING means "관심 지역" example "서울시 영등포구 여의도동 123-45",
                ),
            responseSchema = "SignUpResponse",
            responseBody =
                responseBody(
                    "message" type STRING means "회원가입 메시지",
                ),
        )
    }

    @DisplayName("로그아웃 - 200")
    @Test
    fun t6() {
        every { authenticationService.logout(any()) } returns "userKey"

        val request = TokenRequest("accessToken")

        val response =
            given()
                .contentType(ContentType.JSON)
                .body(request)
                .post("/api/v1/auth/logout")
                .then()
                .status(HttpStatus.OK)
                .contentType(ContentType.JSON)

        response.makeDocument(
            "로그아웃",
            DocsTag.AUTH,
            requestBody =
                requestBody(
                    "token" type STRING means "액세스 토큰" example "accessToken",
                ),
            responseSchema = "LogoutResponse",
            responseBody =
                responseBody(
                    "message" type STRING means "로그아웃 메시지",
                ),
        )
    }

    @DisplayName("토큰 재발급 - 200")
    @Test
    fun t7() {
        val token = Token("newAccessToken", "newRefreshToken")

        every { authenticationService.renew(any()) } returns token

        val request = RefreshTokenRequest("refreshToken")

        val response =
            given()
                .contentType(ContentType.JSON)
                .body(request)
                .post("/api/v1/auth/reissue")
                .then()
                .status(HttpStatus.OK)
                .contentType(ContentType.JSON)

        response.makeDocument(
            "토큰 재발급",
            DocsTag.AUTH,
            requestBody =
                requestBody(
                    "refreshToken" type STRING means "리프레시 토큰" example "refreshToken",
                ),
            responseSchema = "TokenResponse",
            responseBody =
                responseBody(
                    "isTemporaryToken" type BOOLEAN means "임시 토큰 여부",
                    "accessToken" type STRING means "액세스 토큰",
                    "refreshToken" type STRING means "리프레시 토큰",
                ),
        )
    }
}
