package com.sseudam.auth

import com.sseudam.auth.command.CredentialSocialCommand
import com.sseudam.auth.result.CreateSocialUserResult
import com.sseudam.auth.result.SocialLoginResult
import com.sseudam.user.SocialUser
import com.sseudam.user.UserService
import com.sseudam.user.command.UserCommand
import org.springframework.stereotype.Service

@Service
class AuthenticationFacade(
    private val userService: UserService,
    private val authenticationService: AuthenticationService,
) {
    fun socialLogin(
        deviceId: String,
        credentialSocial: CredentialSocialCommand,
    ): SocialLoginResult {
        val existingUser = userService.getSocialUserByEmail(credentialSocial.email)

        val socialUserResult =
            if (existingUser != null) {
                CreateSocialUserResult(socialUser = existingUser, isNewUser = false)
            } else {
                createNewSocialUser(credentialSocial)
            }

        val isNewUser = socialUserResult.isNewUser || socialUserResult.socialUser.name.isNullOrBlank()

        val token =
            authenticationService.socialLogin(
                deviceId = deviceId,
                socialUser = socialUserResult.socialUser,
            )

        return SocialLoginResult(token = token, isNewUser = isNewUser)
    }

    fun createNewSocialUser(credentialSocial: CredentialSocialCommand): CreateSocialUserResult {
        val userCommand =
            userService.create(
                UserCommand(
                    name = credentialSocial.name,
                    email = credentialSocial.email,
                    socialId = credentialSocial.socialId,
                    socialType = credentialSocial.socialType,
                    address = null,
                ),
            )

        val socialUser =
            SocialUser(
                id = userCommand.id,
                key = userCommand.key,
                name = credentialSocial.name,
                socialId = credentialSocial.socialId,
                socialType = credentialSocial.socialType,
            )

        return CreateSocialUserResult(socialUser = socialUser, isNewUser = true)
    }
}
