package com.sseudam.auth.command

import com.sseudam.auth.Token

data class TokenGenerateCommand(
    val token: Token,
)
