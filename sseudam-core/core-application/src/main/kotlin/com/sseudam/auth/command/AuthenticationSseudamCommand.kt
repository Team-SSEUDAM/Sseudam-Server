package com.sseudam.auth.command

import com.sseudam.auth.dto.GrantedAuthority

data class AuthenticationSseudamCommand(
    val loginId: String,
    val password: String,
    val grantedAuthority: GrantedAuthority,
)
