package com.sseudam.auth.command

import com.sseudam.auth.TokenStatus

data class AuthenticationHistoryCommand(
    val userId: Long,
    val userKey: String,
    val deviceId: String?,
    val tokenGenerateCommand: TokenGenerateCommand,
    val status: TokenStatus,
)
