package com.sseudam.notification.event

import java.time.LocalDateTime

data class ReportDiscordNotificationRequestedEvent(
    val id: Long,
    val userId: Long,
    val nickname: String,
    val reportType: String,
    val reportBody: String,
    val createdAt: LocalDateTime,
)
