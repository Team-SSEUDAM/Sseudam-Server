package com.sseudam.admin.presentation.response

import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "로그아웃 응답 Json")
data class AdminLogoutResponse(
    @Schema(description = "로그아웃 메시지", example = "userKey: 로그아웃 되었습니다.")
    val message: String,
)
