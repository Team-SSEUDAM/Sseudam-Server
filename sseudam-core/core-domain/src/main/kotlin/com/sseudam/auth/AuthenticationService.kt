package com.sseudam.auth

import com.sseudam.auth.token.RefreshToken
import com.sseudam.auth.token.Token
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
        credentialSseudam: CredentialSseudam,
    ): Token =
        authenticationProcessor.socialLogin(
            deviceId = deviceId,
            user = user,
            credentialSseudam = credentialSseudam,
        )

    fun socialLogin(
        deviceId: String,
        socialUser: SocialUser,
    ): Token =
        authenticationProcessor.socialLogin(
            deviceId = deviceId,
            socialUser = socialUser,
        )

    fun renew(refreshToken: RefreshToken): Token = authenticationProcessor.renew(refreshToken.token)

    fun logout(token: String): String = authenticationProcessor.remove(token)

    fun withdrawUser(userKey: String) {
        authenticationProcessor.withdrawal(userKey)
    }
}
