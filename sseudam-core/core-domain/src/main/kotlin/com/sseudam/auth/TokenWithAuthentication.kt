package com.sseudam.auth

data class TokenWithAuthentication(
    val accessToken: String,
    val refreshToken: String,
    val deviceId: String?,
    val provider: ProviderDetail,
)
