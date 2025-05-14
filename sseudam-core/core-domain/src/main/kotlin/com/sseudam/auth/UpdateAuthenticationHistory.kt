package com.sseudam.auth

import com.sseudam.auth.token.NewToken

data class UpdateAuthenticationHistory(
    val userKey: String,
    val deviceId: String?,
    val refreshToken: String,
    val newToken: NewToken,
)
