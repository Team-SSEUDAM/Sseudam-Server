package com.sseudam.auth

import com.sseudam.auth.command.CredentialSocialCommand
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
    ): Pair<Boolean, Token> {
        val existingUser = userService.getSocialUserByEmail(credentialSocial.email)

        val (socialUser, isUserNew) =
            if (existingUser != null) {
                existingUser to false
            } else {
                createNewSocialUser(credentialSocial)
            }

        val isNewUser = isUserNew || socialUser.name.isNullOrBlank()

        val token =
            authenticationService.socialLogin(
                deviceId = deviceId,
                socialUser = socialUser,
            )

        return isNewUser to token
    }

    fun createNewSocialUser(credentialSocial: CredentialSocialCommand): Pair<SocialUser, Boolean> {
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

        return socialUser to true
    }
}
