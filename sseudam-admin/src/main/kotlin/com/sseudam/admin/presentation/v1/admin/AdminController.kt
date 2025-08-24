package com.sseudam.admin.presentation.v1.admin

import com.sseudam.admin.application.AdminFacade
import com.sseudam.admin.presentation.request.AdminLoginRequest
import com.sseudam.admin.presentation.request.AdminRefreshTokenRequest
import com.sseudam.admin.presentation.request.AdminTokenRequest
import com.sseudam.admin.presentation.response.AdminLogoutResponse
import com.sseudam.admin.presentation.response.AdminTokenResponse
import com.sseudam.admin.presentation.v1.annotation.AdminTagDocs
import com.sseudam.admin.presentation.v1.annotation.ApiAdminV1Controller
import io.swagger.v3.oas.annotations.Operation
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody

@AdminTagDocs
@ApiAdminV1Controller
class AdminController(
    private val adminFacade: AdminFacade,
) {
    @Operation(summary = "어드민 로그인", description = "어드민 로그인을 합니다.")
    @PostMapping("/login")
    fun login(
        @RequestBody request: AdminLoginRequest,
    ): AdminTokenResponse {
        val token = adminFacade.login(request.loginId, request.password)
        return AdminTokenResponse.of(token)
    }

    @Operation(summary = "어드민 로그아웃", description = "어드민 로그아웃을 합니다.")
    @PostMapping("/logout")
    fun logout(
        @RequestBody request: AdminTokenRequest,
    ): AdminLogoutResponse {
        adminFacade.logout(request.accessToken)
        return AdminLogoutResponse(
            message = "로그아웃 되었습니다.",
        )
    }

    @Operation(summary = "어드민 토큰 재발급", description = "어드민 토큰을 재발급합니다.")
    @PostMapping("/reissue")
    fun reissueToken(
        @RequestBody request: AdminRefreshTokenRequest,
    ): AdminTokenResponse {
        val token = adminFacade.reissue(request.toRefreshToken())
        return AdminTokenResponse.of(token)
    }
}
