package com.sseudam.notification.event

import java.time.LocalDateTime

data class UserDiscordNotificationRequestedEvent(
    val id: Long,
    val email: String,
    val nickname: String,
    val site: String,
    val createdAt: LocalDateTime,
)
