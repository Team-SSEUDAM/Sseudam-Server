package com.sseudam.presentation.v1.auth.request

import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "리프레시 토큰 요청")
data class RefreshTokenRequest(
    @Schema(description = "리프레시 토큰", example = "refresh_token")
    val refreshToken: String,
)
