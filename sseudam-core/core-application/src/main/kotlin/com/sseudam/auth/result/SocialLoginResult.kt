package com.sseudam.auth.result

import com.sseudam.auth.Token

data class SocialLoginResult(
    val token: Token,
    val isNewUser: Boolean,
)
