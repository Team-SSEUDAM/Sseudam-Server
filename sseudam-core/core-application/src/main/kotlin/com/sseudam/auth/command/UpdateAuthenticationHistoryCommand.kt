package com.sseudam.auth.command

data class UpdateAuthenticationHistoryCommand(
    val userKey: String,
    val deviceId: String?,
    val refreshToken: String,
    val tokenGenerateCommand: TokenGenerateCommand,
)
