package com.sseudam.notification.event

data class ReportFcmNotificationRequestedEvent(
    val userId: Long,
    val title: String,
    val body: String,
    val destination: String,
    val notificationType: String,
    val parameterValue: String,
)
