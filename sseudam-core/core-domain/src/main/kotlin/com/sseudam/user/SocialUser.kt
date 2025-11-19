package com.sseudam.user

import com.sseudam.common.SocialType

data class SocialUser(
    val id: Long,
    val key: String,
    val name: String?,
    val socialId: String,
    val socialType: SocialType,
)
