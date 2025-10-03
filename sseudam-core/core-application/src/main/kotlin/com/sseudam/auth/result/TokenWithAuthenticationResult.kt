package com.sseudam.auth.result

import com.sseudam.auth.dto.ProviderDetail

data class TokenWithAuthenticationResult(
    val accessToken: String,
    val refreshToken: String,
    val deviceId: String?,
    val provider: ProviderDetail,
)
