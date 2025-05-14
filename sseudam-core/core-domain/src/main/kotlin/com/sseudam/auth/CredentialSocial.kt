package com.sseudam.auth

import com.sseudam.user.SocialType

data class CredentialSocial(
    val email: String,
    val name: String?,
    val socialId: String,
    val socialType: SocialType,
)
