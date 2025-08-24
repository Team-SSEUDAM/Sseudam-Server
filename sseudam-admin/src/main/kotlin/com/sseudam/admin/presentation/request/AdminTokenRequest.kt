package com.sseudam.admin.presentation.request

import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "어드민 토큰 요청 Json")
data class AdminTokenRequest(
    @Schema(description = "토큰", example = "access_token")
    val accessToken: String,
)
