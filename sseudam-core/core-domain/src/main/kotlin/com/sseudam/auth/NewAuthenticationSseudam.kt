package com.sseudam.auth

data class NewAuthenticationSseudam(
    val loginId: String,
    val password: String,
    val grantedAuthority: GrantedAuthority,
)
