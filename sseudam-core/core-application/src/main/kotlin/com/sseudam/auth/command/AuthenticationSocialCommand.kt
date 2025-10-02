package com.sseudam.auth.command

import com.sseudam.auth.GrantedAuthority
import com.sseudam.user.SocialType

data class AuthenticationSocialCommand(
    val loginId: String,
    val socialId: String,
    val socialType: SocialType,
    val grantedAuthority: GrantedAuthority,
)
