package com.sseudam.auth.dto

data class ProviderDetail(
    val userId: Long,
    val userKey: String,
    val grantedAuthorities: List<String>,
)
