package com.sseudam.user

data class UserCredentials(
    val id: Long,
    val key: String,
    val loginId: String,
    val password: String,
)
