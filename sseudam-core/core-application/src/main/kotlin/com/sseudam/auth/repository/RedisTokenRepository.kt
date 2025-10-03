package com.sseudam.auth.repository

import com.sseudam.auth.dto.Provider
import com.sseudam.auth.dto.ProviderDetail
import com.sseudam.auth.result.TokenWithAuthenticationResult

interface RedisTokenRepository {
    fun create(
        accessToken: String,
        refreshToken: String,
        deviceId: String?,
        providerDetail: ProviderDetail,
        accessTokenExpiration: Long,
        refreshTokenExpiration: Long,
    ): TokenWithAuthenticationResult

    fun findByToken(token: String): TokenWithAuthenticationResult

    fun findBy(accessToken: String): Provider?

    fun deleteToken(token: String)

    fun deleteAllToken(token: String)
}
