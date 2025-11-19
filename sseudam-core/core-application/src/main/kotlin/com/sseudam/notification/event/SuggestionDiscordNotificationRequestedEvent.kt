package com.sseudam.notification.event

import java.time.LocalDateTime

data class SuggestionDiscordNotificationRequestedEvent(
    val id: Long,
    val site: String,
    val spotName: String,
    val trashType: String,
    val userId: Long,
    val nickname: String,
    val coordinateText: String,
    val createdAt: LocalDateTime,
)
