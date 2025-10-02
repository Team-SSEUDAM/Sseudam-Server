package com.sseudam.auth

import com.sseudam.auth.command.CredentialSseudamCommand
import com.sseudam.auth.component.AuthenticationProcessor
import com.sseudam.auth.dto.GrantedAuthority
import com.sseudam.user.SocialUser
import com.sseudam.user.User
import org.springframework.stereotype.Service

@Service
class AuthenticationService(
    private val authenticationProcessor: AuthenticationProcessor,
) {
    fun login(
        deviceId: String?,
        user: User,
        credentialSseudamCommand: CredentialSseudamCommand,
    ): Token =
        authenticationProcessor.login(
            deviceId = deviceId,
            user = user,
            credentialSseudamCommand = credentialSseudamCommand,
        )

    fun socialLogin(
        deviceId: String,
        socialUser: SocialUser,
    ): Token =
        authenticationProcessor.login(
            deviceId = deviceId,
            socialUser = socialUser,
        )

    fun renew(refreshToken: String): Token = authenticationProcessor.renew(refreshToken)

    fun logout(token: String): String = authenticationProcessor.remove(token)

    fun withdrawUser(userKey: String) {
        authenticationProcessor.withdrawal(userKey)
    }

    fun adminLogin(adminId: Long): Token =
        authenticationProcessor.adminLogin(
            adminId,
            listOf(
                GrantedAuthority(
                    AuthorityType.ADMIN,
                ),
            ),
        )

    fun adminReissue(refreshToken: String): Token = authenticationProcessor.adminRenew(refreshToken)

    fun adminLogout(accessToken: String) = authenticationProcessor.adminLogout(accessToken)
}
