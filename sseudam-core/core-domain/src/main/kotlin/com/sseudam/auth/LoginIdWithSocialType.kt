package com.sseudam.auth

import com.sseudam.user.SocialType
import java.time.LocalDateTime

data class LoginIdWithSocialType(
    val loginId: String,
    val socialType: SocialType,
    val createdAt: LocalDateTime,
)
