package com.sseudam.storage.redis

import com.fasterxml.jackson.databind.ObjectMapper
import com.sseudam.auth.Provider
import com.sseudam.auth.ProviderDetail
import com.sseudam.auth.RedisTokenRepository
import com.sseudam.auth.TokenWithAuthentication
import com.sseudam.support.error.AuthenticationErrorException
import com.sseudam.support.error.AuthenticationErrorType
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Repository
import java.time.Duration

@Repository
class RedisTokenCoreRepository(
    @Qualifier("authenticationRedisTemplate")
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
    ): TokenWithAuthentication {
        val tokenWithAuthentication =
            TokenWithAuthentication(
                accessToken = accessToken,
                refreshToken = refreshToken,
                deviceId = deviceId,
                provider = providerDetail,
            )

        redisTemplate.opsForValue().apply {
            set(
                accessToken,
                objectMapper.writeValueAsString(tokenWithAuthentication),
                Duration.ofSeconds(accessTokenExpiration * 60L),
            )
            set(
                refreshToken,
                objectMapper.writeValueAsString(tokenWithAuthentication),
                Duration.ofSeconds(refreshTokenExpiration * 60L),
            )
        }
        return tokenWithAuthentication
    }

    override fun findByToken(token: String): TokenWithAuthentication {
        redisTemplate.opsForValue().get(token)?.let {
            return objectMapper.readValue(it, TokenWithAuthentication::class.java)
        } ?: throw AuthenticationErrorException(AuthenticationErrorType.INVALID_TOKEN)
    }

    override fun findBy(accessToken: String): Provider? =
        redisTemplate.opsForValue().get(accessToken)?.let {
            val tokenWithAuthentication = objectMapper.readValue(it, TokenWithAuthentication::class.java)
            Provider(
                userId = tokenWithAuthentication.provider.userId,
                userKey = tokenWithAuthentication.provider.userKey,
            )
        }

    override fun deleteToken(token: String) {
        redisTemplate.delete(token)
    }

    override fun deleteAllToken(token: String) {
        val tokenWithAuthentication =
            redisTemplate.opsForValue().get(token)?.let {
                objectMapper.readValue(it, TokenWithAuthentication::class.java)
            } ?: throw AuthenticationErrorException(AuthenticationErrorType.INVALID_TOKEN)

        redisTemplate.delete(tokenWithAuthentication.accessToken)
        redisTemplate.delete(tokenWithAuthentication.refreshToken)
    }
}
