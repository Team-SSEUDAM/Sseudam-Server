package com.sseudam.auth.component

import com.sseudam.auth.Token
import com.sseudam.auth.TokenStatus
import com.sseudam.auth.command.AuthenticationHistoryCommand
import com.sseudam.auth.command.CredentialSseudamCommand
import com.sseudam.auth.command.TokenGenerateCommand
import com.sseudam.auth.dto.GrantedAuthority
import com.sseudam.auth.repository.TokenRepository
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
        credentialSseudamCommand: CredentialSseudamCommand,
    ): Token =
        tokenRepository.create(deviceId, user).apply {
            authenticationHistoryWriter.write(
                AuthenticationHistoryCommand(
                    userId = user.id,
                    userKey = user.key,
                    deviceId = deviceId,
                    tokenGenerateCommand =
                        TokenGenerateCommand(
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
                AuthenticationHistoryCommand(
                    userId = socialUser.id,
                    userKey = socialUser.key,
                    deviceId = deviceId,
                    tokenGenerateCommand =
                        TokenGenerateCommand(
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

    fun adminLogout(accessToken: String) = tokenRepository.adminTokenRemove(accessToken)
}
