package com.sseudam.notification.command

data class CreateNotificationStoredCommand(
    val notificationStoredKey: String,
    val userId: Long,
    val type: String,
    val parameterValue: String,
    val topic: String,
    val contents: String,
)
