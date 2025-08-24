package com.sseudam.notification

data class SendNotificationMessage(
    val userId: Long,
    val title: String,
    val body: String,
)
