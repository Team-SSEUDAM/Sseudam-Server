package com.sseudam.auth.dto

import com.sseudam.auth.AuthorityType

data class GrantedAuthority(
    val authorityType: AuthorityType,
)
