package com.sseudam.presentation.v1.auth.request

import com.sseudam.auth.CredentialSseudam
import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "로그인 요청")
data class LoginRequest(
    @Schema(description = "로그인 아이디", example = "test@test.com")
    val loginId: String,
    @Schema(description = "비밀번호", example = "password")
    val password: String,
) {
    fun toCredentialSseudam(): CredentialSseudam =
        CredentialSseudam(
            loginId = loginId,
            password = password,
        )
}
