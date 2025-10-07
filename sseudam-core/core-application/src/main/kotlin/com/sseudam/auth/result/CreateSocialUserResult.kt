package com.sseudam.auth.result

import com.sseudam.user.SocialUser

data class CreateSocialUserResult(
    val socialUser: SocialUser,
    val isNewUser: Boolean,
)
