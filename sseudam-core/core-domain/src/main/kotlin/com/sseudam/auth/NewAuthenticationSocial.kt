package com.sseudam.auth

import com.sseudam.user.SocialType

data class NewAuthenticationSocial(
    val loginId: String,
    val socialId: String,
    val socialType: SocialType,
    val grantedAuthority: GrantedAuthority,
)
