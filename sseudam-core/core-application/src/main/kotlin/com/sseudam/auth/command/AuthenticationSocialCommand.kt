package com.sseudam.auth.command

import com.sseudam.auth.dto.GrantedAuthority
import com.sseudam.common.SocialType

data class AuthenticationSocialCommand(
    val loginId: String,
    val socialId: String,
    val socialType: SocialType,
    val grantedAuthority: GrantedAuthority,
)
