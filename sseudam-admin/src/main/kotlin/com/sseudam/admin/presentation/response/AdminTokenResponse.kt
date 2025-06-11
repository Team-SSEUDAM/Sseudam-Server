package com.sseudam.admin.presentation.response

data class AdminTokenResponse(
    val accessToken: String,
    val refreshToken: String,
)
