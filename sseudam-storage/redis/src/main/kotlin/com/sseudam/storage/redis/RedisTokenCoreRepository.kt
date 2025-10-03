package com.sseudam.storage.redis

import com.fasterxml.jackson.databind.ObjectMapper
import com.sseudam.auth.dto.Provider
import com.sseudam.auth.dto.ProviderDetail
import com.sseudam.auth.repository.RedisTokenRepository
import com.sseudam.auth.result.TokenWithAuthenticationResult
import com.sseudam.support.error.AuthenticationErrorException
import com.sseudam.support.error.AuthenticationErrorType
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Repository
import java.time.Duration

@Repository
class RedisTokenCoreRepository(
    private val redisTemplate: RedisTemplate<String, String>,
    private val objectMapper: ObjectMapper,
) : RedisTokenRepository {
    override fun create(
        accessToken: String,
        refreshToken: String,
        deviceId: String?,
        providerDetail: ProviderDetail,
        accessTokenExpiration: Long,
        refreshTokenExpiration: Long,
    ): TokenWithAuthenticationResult {
        val tokenWithAuthenticationResult =
            TokenWithAuthenticationResult(
                accessToken = accessToken,
                refreshToken = refreshToken,
                deviceId = deviceId,
                provider = providerDetail,
            )

        redisTemplate.opsForValue().apply {
            set(
                accessToken,
                objectMapper.writeValueAsString(tokenWithAuthenticationResult),
                Duration.ofSeconds(accessTokenExpiration * 60L),
            )
            set(
                refreshToken,
                objectMapper.writeValueAsString(tokenWithAuthenticationResult),
                Duration.ofSeconds(refreshTokenExpiration * 60L),
            )
        }
        return tokenWithAuthenticationResult
    }

    override fun findByToken(token: String): TokenWithAuthenticationResult {
        redisTemplate.opsForValue().get(token)?.let {
            return objectMapper.readValue(it, TokenWithAuthenticationResult::class.java)
        } ?: throw AuthenticationErrorException(AuthenticationErrorType.INVALID_TOKEN)
    }

    override fun findBy(accessToken: String): Provider? =
        redisTemplate.opsForValue().get(accessToken)?.let {
            val tokenWithAuthenticationResult = objectMapper.readValue(it, TokenWithAuthenticationResult::class.java)
            Provider(
                userId = tokenWithAuthenticationResult.provider.userId,
                userKey = tokenWithAuthenticationResult.provider.userKey,
            )
        }

    override fun deleteToken(token: String) {
        redisTemplate.delete(token)
    }

    override fun deleteAllToken(token: String) {
        val tokenWithAuthenticationResult =
            redisTemplate.opsForValue().get(token)?.let {
                objectMapper.readValue(it, TokenWithAuthenticationResult::class.java)
            }

        tokenWithAuthenticationResult?.accessToken?.let { redisTemplate.delete(it) }
        tokenWithAuthenticationResult?.refreshToken?.let { redisTemplate.delete(it) }
    }
}
