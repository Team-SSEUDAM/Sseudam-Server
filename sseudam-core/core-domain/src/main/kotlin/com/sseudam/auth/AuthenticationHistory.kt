package com.sseudam.auth

import com.sseudam.auth.token.Token
import com.sseudam.auth.token.TokenStatus
import java.time.LocalDateTime

data class AuthenticationHistory(
    val authenticationId: Long,
    val userKey: String,
    val deviceId: String?,
    val token: Token,
    val status: TokenStatus,
    val loggedInAt: LocalDateTime,
)
