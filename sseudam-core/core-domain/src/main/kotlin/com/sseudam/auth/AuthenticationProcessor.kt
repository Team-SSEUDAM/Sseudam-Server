package com.sseudam.auth

import com.sseudam.auth.token.NewToken
import com.sseudam.auth.token.Token
import com.sseudam.auth.token.TokenStatus
import com.sseudam.auth.token.repository.TokenRepository
import com.sseudam.user.SocialUser
import com.sseudam.user.User
import org.springframework.stereotype.Component

@Component
class AuthenticationProcessor(
    private val tokenRepository: TokenRepository,
    private val authenticationHistoryWriter: AuthenticationHistoryWriter,
) {
    fun login(
        deviceId: String?,
        user: User,
        credentialSseudam: CredentialSseudam,
    ): Token =
        tokenRepository.create(deviceId, user).apply {
            authenticationHistoryWriter.write(
                NewAuthenticationHistory(
                    userId = user.id,
                    userKey = user.key,
                    deviceId = deviceId,
                    newToken =
                        NewToken(
                            Token(
                                accessToken = this.accessToken,
                                refreshToken = this.refreshToken,
                            ),
                        ),
                    status = TokenStatus.ACTIVE,
                ),
            )
        }

    fun login(
        deviceId: String,
        socialUser: SocialUser,
    ): Token =
        tokenRepository.create(deviceId, socialUser).apply {
            authenticationHistoryWriter.write(
                NewAuthenticationHistory(
                    userId = socialUser.id,
                    userKey = socialUser.key,
                    deviceId = deviceId,
                    newToken =
                        NewToken(
                            Token(
                                accessToken = this.accessToken,
                                refreshToken = this.refreshToken,
                            ),
                        ),
                    status = TokenStatus.ACTIVE,
                ),
            )
        }

    fun renew(refreshToken: String): Token = tokenRepository.renew(refreshToken)

    fun remove(token: String): String = tokenRepository.remove(token)

    fun withdrawal(userKey: String) {
        tokenRepository.removeByUserKey(userKey)
    }

    fun adminLogin(
        adminId: Long,
        grantedAuthorities: List<GrantedAuthority>,
    ): Token = tokenRepository.create(adminId, grantedAuthorities)

    fun adminRenew(refreshToken: String): Token = tokenRepository.adminRefresh(refreshToken)
}
