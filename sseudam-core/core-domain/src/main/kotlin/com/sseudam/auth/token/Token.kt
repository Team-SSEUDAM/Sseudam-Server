package com.sseudam.auth.token

data class Token(
    val accessToken: String,
    val refreshToken: String,
)
