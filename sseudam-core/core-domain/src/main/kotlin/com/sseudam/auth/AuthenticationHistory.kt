package com.sseudam.auth

import java.time.LocalDateTime

data class AuthenticationHistory(
    val authenticationId: Long,
    val userKey: String,
    val deviceId: String?,
    val token: Token,
    val status: TokenStatus,
    val loggedInAt: LocalDateTime,
)
