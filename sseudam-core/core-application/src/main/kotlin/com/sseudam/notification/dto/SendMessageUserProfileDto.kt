package com.sseudam.notification.dto

import java.time.LocalDateTime

data class SendMessageUserProfileDto(
    val id: Long,
    val email: String,
    val nickname: String,
    val site: String,
    val createdAt: LocalDateTime,
)
