package com.sseudam.notification.command

import com.sseudam.notification.NotificationType
import com.sseudam.notification.ReadStatus

data class CreateNotificationStoredCommand(
    val notificationStoredKey: String,
    val userId: Long,
    val type: NotificationType,
    val parameterValue: String,
    val topic: String,
    val contents: String,
    val readStatus: ReadStatus = ReadStatus.UNREAD,
)
