package com.sseudam.presentation.v1.auth

import com.sseudam.auth.AuthenticationFacade
import com.sseudam.auth.AuthenticationService
import com.sseudam.auth.CredentialSocial
import com.sseudam.client.oauth.OAuthService
import com.sseudam.common.Address
import com.sseudam.presentation.v1.annotation.ApiV1Controller
import com.sseudam.presentation.v1.auth.request.LoginRequest
import com.sseudam.presentation.v1.auth.request.RefreshTokenRequest
import com.sseudam.presentation.v1.auth.request.SignUpRequest
import com.sseudam.presentation.v1.auth.request.SignUpSocialRequest
import com.sseudam.presentation.v1.auth.request.TokenRequest
import com.sseudam.presentation.v1.auth.response.LogoutResponse
import com.sseudam.presentation.v1.auth.response.SignUpResponse
import com.sseudam.presentation.v1.auth.response.TokenResponse
import com.sseudam.support.error.ErrorException
import com.sseudam.support.error.ErrorType
import com.sseudam.user.SocialType
import com.sseudam.user.User
import com.sseudam.user.UserService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader

@Tag(name = "\uD83D\uDD11 Auth API", description = "인증 관련 API")
@ApiV1Controller
class AuthController(
    private val passwordEncoder: PasswordEncoder,
    private val authenticationService: AuthenticationService,
    private val authenticationFacade: AuthenticationFacade,
    private val oAuthService: OAuthService,
    private val userService: UserService,
) {
    @Operation(summary = "테스트 로그인", description = "테스트를 위한 로그인을 합니다.")
    @PostMapping("/auth/test-login")
    fun login(
        @RequestHeader(name = "X-DEVICE-ID") deviceId: String?,
        @RequestBody request: LoginRequest,
    ): TokenResponse {
        val userCredentials = userService.getUserCredential(request.loginId)
        if (!passwordEncoder.matches(request.password, userCredentials.password)) {
            throw ErrorException(ErrorType.INVALID_PASSWORD)
        }
        val token = authenticationService.login(deviceId, User(userCredentials.id, userCredentials.key), request.toCredentialSseudam())
        return TokenResponse.toResponse(false, token)
    }

    @Operation(summary = "이메일 회원가입", description = "회원가입합니다.")
    @PostMapping("/auth/signup")
    fun signUp(
        @RequestBody request: SignUpRequest,
    ): SignUpResponse {
        userService.create(request.toNewUser(passwordEncoder.encode(request.password)))
        return SignUpResponse("회원가입에 성공했습니다.")
    }

    @Operation(summary = "카카오 로그인", description = "카카오 소셜 로그인합니다.")
    @PostMapping("/auth/social-login/kakao")
    fun socialKakaoLogin(
        @RequestBody request: TokenRequest,
    ): TokenResponse {
        val socialInfo = oAuthService.getKaKaoUserInfo(request.token)
        val (isTemporaryToken, token) =
            authenticationFacade.socialLogin(
                // TODO: deviceId 는 추후에 추가
                deviceId = "",
                credentialSocial =
                    CredentialSocial(
                        email = socialInfo.email,
                        name = socialInfo.name,
                        socialId = socialInfo.id,
                        socialType = SocialType.KAKAO,
                    ),
            )
        return TokenResponse.toResponse(isTemporaryToken, token)
    }

    @Operation(summary = "애플 소셜 로그인", description = "애플 소셜 로그인합니다.")
    @PostMapping("/auth/social-login/apple")
    fun socialAppleLogin(
        @RequestBody request: TokenRequest,
    ): TokenResponse {
        val socialInfo = oAuthService.getAppleUserInfo(request.token)
        val (isTemporaryToken, token) =
            authenticationFacade.socialLogin(
                // TODO: deviceId 는 추후에 추가
                deviceId = "",
                credentialSocial =
                    CredentialSocial(
                        email = socialInfo.email,
                        name = null,
                        socialId = socialInfo.id,
                        socialType = SocialType.APPLE,
                    ),
            )
        return TokenResponse.toResponse(isTemporaryToken, token)
    }

    @Operation(summary = "소셜 회원가입", description = "소셜 회원 가입합니다.")
    @PostMapping("/auth/social-signup")
    fun socialSignUp(
        @RequestBody request: SignUpSocialRequest,
    ): SignUpResponse {
        val tempUser = userService.getSocialUserByEmail(request.email)
        if (tempUser == null) {
            throw ErrorException(ErrorType.NOT_FOUND_DATA)
        } else {
            userService.updateName(tempUser.key, request.name)
            userService.updateAddress(
                tempUser.key,
                Address(
                    city = request.address.split(" ")[1],
                    site = request.address,
                ),
            )
        }
        return SignUpResponse("회원가입에 성공했습니다.")
    }

    @Operation(summary = "로그아웃", description = "로그아웃합니다.")
    @PostMapping("/auth/logout")
    fun logout(
        @RequestBody request: TokenRequest,
    ): LogoutResponse {
        val logoutUserKey = authenticationService.logout(request.token)
        return LogoutResponse("$logoutUserKey: 로그아웃 되었습니다.")
    }

    @Operation(summary = "토큰 재발급", description = "토큰을 재발급합니다.")
    @PostMapping("/auth/reissue")
    fun reissueToken(
        @RequestBody request: RefreshTokenRequest,
    ): TokenResponse {
        val token = authenticationService.renew(request.toRefreshToken())
        return TokenResponse.toResponse(false, token)
    }
}
