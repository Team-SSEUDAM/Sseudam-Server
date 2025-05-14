package com.sseudam.auth

import com.sseudam.auth.token.NewToken
import com.sseudam.auth.token.TokenStatus

data class NewAuthenticationHistory(
    val userId: Long,
    val userKey: String,
    val deviceId: String?,
    val newToken: NewToken,
    val status: TokenStatus,
)
