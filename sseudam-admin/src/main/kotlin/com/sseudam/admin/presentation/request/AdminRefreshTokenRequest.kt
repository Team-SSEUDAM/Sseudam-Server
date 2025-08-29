package com.sseudam.admin.presentation.request

import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "어드민 리프레시 토큰 요청 Json")
data class AdminRefreshTokenRequest(
    @Schema(description = "리프레시 토큰", example = "refresh_token")
    val refreshToken: String,
)
