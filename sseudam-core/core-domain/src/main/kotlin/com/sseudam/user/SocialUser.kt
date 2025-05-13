package com.sseudam.user

data class SocialUser(
    val id: Long,
    val key: String,
    val name: String?,
    val socialId: String,
    val socialType: SocialType,
)
