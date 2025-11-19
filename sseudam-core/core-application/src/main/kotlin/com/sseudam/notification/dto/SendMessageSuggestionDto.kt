package com.sseudam.notification.dto

import java.time.LocalDateTime

data class SendMessageSuggestionDto(
    val id: Long,
    val site: String,
    val spotName: String,
    val trashType: String,
    val userId: Long,
    val nickname: String,
    val coordinateText: String,
    val createdAt: LocalDateTime,
)
