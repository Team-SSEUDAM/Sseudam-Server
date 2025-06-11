package com.sseudam.admin.domain

data class AdminToken(
    val accessToken: String,
    val refreshToken: String,
)
