package com.sseudam.notification.dto

import java.time.LocalDateTime

data class SendMessageReportDto(
    val id: Long,
    val userId: Long,
    val nickname: String,
    val reportType: String,
    val reportBody: String,
    val createdAt: LocalDateTime,
)
