package com.sseudam.auth

data class ProviderDetail(
    val userId: Long,
    val userKey: String,
    val grantedAuthorities: List<String>,
)
