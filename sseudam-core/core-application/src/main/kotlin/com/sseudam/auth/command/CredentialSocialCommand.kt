package com.sseudam.auth.command

import com.sseudam.common.SocialType

data class CredentialSocialCommand(
    val email: String,
    val name: String,
    val socialId: String,
    val socialType: SocialType,
)
