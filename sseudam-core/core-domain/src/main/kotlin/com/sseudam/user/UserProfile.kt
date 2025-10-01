package com.sseudam.user

import com.sseudam.common.Address
import java.time.LocalDateTime

data class UserProfile(
    val id: Long,
    val key: String,
    val email: String,
    val name: String?,
    val nickname: String,
    val address: Address,
    val createdAt: LocalDateTime,
)
