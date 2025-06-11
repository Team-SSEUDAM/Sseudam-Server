package com.sseudam.jwt

import com.sseudam.auth.AuthenticationHistory
import com.sseudam.auth.AuthenticationHistoryReader
import com.sseudam.auth.AuthenticationHistoryUpdater
import com.sseudam.auth.AuthorityType
import com.sseudam.auth.GrantedAuthority
import com.sseudam.auth.Provider
import com.sseudam.auth.ProviderDetail
import com.sseudam.auth.RedisTokenRepository
import com.sseudam.auth.UpdateAuthenticationHistory
import com.sseudam.auth.token.NewToken
import com.sseudam.auth.token.Token
import com.sseudam.auth.token.TokenStatus
import com.sseudam.auth.token.repository.TokenRepository
import com.sseudam.config.AuthenticationProperties
import com.sseudam.support.error.AuthenticationErrorException
import com.sseudam.support.error.AuthenticationErrorType
import com.sseudam.user.SocialUser
import com.sseudam.user.User
import org.springframework.security.authentication.AuthenticationServiceException
import org.springframework.security.oauth2.jwt.BadJwtException
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.security.oauth2.jwt.JwtClaimsSet
import org.springframework.security.oauth2.jwt.JwtDecoder
import org.springframework.security.oauth2.jwt.JwtEncoder
import org.springframework.security.oauth2.jwt.JwtEncoderParameters
import org.springframework.security.oauth2.jwt.JwtException
import org.springframework.security.oauth2.server.resource.InvalidBearerTokenException
import org.springframework.stereotype.Component
import java.time.Instant
import javax.naming.AuthenticationException

@Component
class JwtProvider(
    private val jwtEncoder: JwtEncoder,
    private val jwtDecoder: JwtDecoder,
    private val authenticationProperties: AuthenticationProperties,
    private val redisTokenRepository: RedisTokenRepository,
    private val authenticationHistoryReader: AuthenticationHistoryReader,
    private val authenticationHistoryUpdater: AuthenticationHistoryUpdater,
) : TokenRepository {
    companion object {
        val grantedAuthorities = listOf(GrantedAuthority(AuthorityType.USER))
    }

    override fun create(
        deviceId: String?,
        user: User,
    ): Token {
        val accessToken =
            issueAccessToken(
                user.key,
                grantedAuthorities,
            )
        val refreshToken = issueRefreshToken(user.key)
        return Token(
            accessToken = accessToken,
            refreshToken = refreshToken,
        ).apply {
            redisTokenRepository.create(
                accessToken = this.accessToken,
                refreshToken = this.refreshToken,
                deviceId = deviceId,
                providerDetail =
                    ProviderDetail(
                        user.id,
                        user.key,
                        grantedAuthorities =
                            grantedAuthorities.map {
                                it.authorityType.name
                            },
                    ),
                accessTokenExpiration = authenticationProperties.accessTokenExpirationSeconds,
                refreshTokenExpiration = authenticationProperties.refreshTokenExpirationSeconds,
            )
        }
    }

    override fun create(
        deviceId: String?,
        socialUser: SocialUser,
    ): Token {
        val accessToken =
            issueAccessToken(
                socialUser.key,
                grantedAuthorities,
            )
        val refreshToken = issueRefreshToken(socialUser.key)
        return Token(
            accessToken = accessToken,
            refreshToken = refreshToken,
        ).apply {
            redisTokenRepository.create(
                accessToken = this.accessToken,
                refreshToken = this.refreshToken,
                deviceId = deviceId,
                providerDetail =
                    ProviderDetail(
                        socialUser.id,
                        socialUser.key,
                        grantedAuthorities =
                            grantedAuthorities.map {
                                it.authorityType.name
                            },
                    ),
                accessTokenExpiration = authenticationProperties.accessTokenExpirationSeconds,
                refreshTokenExpiration = authenticationProperties.refreshTokenExpirationSeconds,
            )
        }
    }

    override fun create(
        adminId: Long,
        grantedAuthorities: List<GrantedAuthority>,
    ): Token {
        val accessToken =
            issueAccessToken(
                adminId.toString(),
                grantedAuthorities,
            )
        val refreshToken = issueRefreshToken(adminId.toString())
        return Token(
            accessToken = accessToken,
            refreshToken = refreshToken,
        ).apply {
            redisTokenRepository.create(
                accessToken = this.accessToken,
                refreshToken = this.refreshToken,
                deviceId = null,
                providerDetail =
                    ProviderDetail(
                        adminId,
                        adminId.toString(),
                        grantedAuthorities =
                            grantedAuthorities.map {
                                it.authorityType.name
                            },
                    ),
                accessTokenExpiration = authenticationProperties.accessTokenExpirationSeconds,
                refreshTokenExpiration = authenticationProperties.refreshTokenExpirationSeconds,
            )
        }
    }

    override fun renew(refreshToken: String): Token {
        val jwt = validateToken(refreshToken)
        val tokenWithAuthentication = redisTokenRepository.findByToken(jwt.tokenValue)
        val authenticationHistory =
            verifyTokenHistory(
                userKey = tokenWithAuthentication.provider.userKey,
                deviceId = tokenWithAuthentication.deviceId,
                refreshToken = tokenWithAuthentication.refreshToken,
            )

        removeRotationToken(tokenWithAuthentication.accessToken, tokenWithAuthentication.refreshToken)

        val newAccessToken =
            issueAccessToken(
                jwtId = tokenWithAuthentication.provider.userKey,
                grantedAuthorities =
                    tokenWithAuthentication.provider.grantedAuthorities.map {
                        GrantedAuthority(AuthorityType.valueOf(it))
                    },
            )
        val newRefreshToken =
            issueRefreshToken(
                jwtId = tokenWithAuthentication.provider.userKey,
            )

        return Token(
            accessToken = newAccessToken,
            refreshToken = newRefreshToken,
        ).apply {
            redisTokenRepository.create(
                accessToken = this.accessToken,
                refreshToken = this.refreshToken,
                deviceId = tokenWithAuthentication.deviceId,
                providerDetail = tokenWithAuthentication.provider,
                accessTokenExpiration = authenticationProperties.accessTokenExpirationSeconds,
                refreshTokenExpiration = authenticationProperties.refreshTokenExpirationSeconds,
            )

            authenticationHistoryUpdater.update(
                UpdateAuthenticationHistory(
                    userKey = authenticationHistory.userKey,
                    deviceId = authenticationHistory.deviceId,
                    refreshToken = refreshToken,
                    newToken =
                        NewToken(
                            token =
                                Token(
                                    accessToken = this.accessToken,
                                    refreshToken = this.refreshToken,
                                ),
                        ),
                ),
            )
        }
    }

    override fun remove(token: String): String {
        val jwt = validateToken(token)
        authenticationHistoryUpdater.remove(token).apply {
            redisTokenRepository.deleteAllToken(this)
        }
        return jwt.id
    }

    override fun removeByUserKey(userKey: String) {
        authenticationHistoryUpdater.removeToken(userKey).map {
            redisTokenRepository.deleteAllToken(it)
        }
    }

    override fun findBy(accessToken: String): Provider? = redisTokenRepository.findBy(accessToken)

    @Throws(AuthenticationException::class)
    fun validateToken(token: String): Jwt =
        try {
            jwtDecoder.decode(token)
        } catch (exception: BadJwtException) {
            throw AuthenticationErrorException(AuthenticationErrorType.INVALID_TOKEN)
        } catch (exception: JwtException) {
            throw AuthenticationServiceException(exception.message, exception)
        }

    /**
     * issue access token
     *
     * @param jwtId [String] jwt identifier.
     */
    private fun issueAccessToken(
        jwtId: String,
        grantedAuthorities: List<GrantedAuthority>,
    ): String {
        val issuedAt: Instant = Instant.now()
        return generateToken(
            jwtId = jwtId,
            expiresAt = issuedAt.plusSeconds(authenticationProperties.accessTokenExpirationSeconds * 60L),
            issuedAt = issuedAt,
            claims = mapOf(Pair("type", "A"), Pair("roles", grantedAuthorities.map { it.authorityType.name })),
        )
    }

    /**
     * issue refresh token
     *
     * @param jwtId [String] jwt identifier.
     */
    private fun issueRefreshToken(jwtId: String): String {
        val issuedAt: Instant = Instant.now()
        return generateToken(
            jwtId = jwtId,
            expiresAt = issuedAt.plusSeconds(authenticationProperties.refreshTokenExpirationSeconds * 60L),
            issuedAt = issuedAt,
            claims = mapOf(Pair("type", "R")),
        )
    }

    /**
     * Generate Token
     *
     * @param jwtId [String] jwt identifier.
     * @param expiresAt [Instant] token's expiresAt.
     * @param issuedAt [Instant] token's issuedAt.
     * @param claims [Map] token's claims.
     */
    private fun generateToken(
        jwtId: String,
        expiresAt: Instant,
        issuedAt: Instant,
        claims: Map<String, Any>? = emptyMap(),
    ): String {
        val jwtClaimsSet: JwtClaimsSet =
            JwtClaimsSet
                .builder()
                .id(jwtId)
                .expiresAt(expiresAt)
                .issuedAt(issuedAt)
                .issuer("sseudam")
                .claims {
                    if (claims != null) {
                        it.putAll(claims)
                    }
                }.build()
        try {
            return jwtEncoder.encode(JwtEncoderParameters.from(jwtClaimsSet)).tokenValue
        } catch (e: IllegalArgumentException) {
            throw InvalidBearerTokenException(e.message)
        }
    }

    private fun verifyTokenHistory(
        userKey: String,
        deviceId: String?,
        refreshToken: String,
    ): AuthenticationHistory {
        val authenticationHistory =
            authenticationHistoryReader.readByUserKeyWithDeviceWithRefreshToken(
                userKey = userKey,
                deviceId = deviceId,
                refreshToken = refreshToken,
            )

        if (authenticationHistory.status == TokenStatus.INACTIVE) {
            throw AuthenticationErrorException(AuthenticationErrorType.INVALID_TOKEN)
        }
        return authenticationHistory
    }

    private fun removeRotationToken(
        accessToken: String,
        refreshToken: String,
    ) {
        redisTokenRepository.deleteToken(accessToken)
        redisTokenRepository.deleteToken(refreshToken)
    }
}
