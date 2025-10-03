package com.sseudam.auth

data class Token(
    val accessToken: String,
    val refreshToken: String,
)
