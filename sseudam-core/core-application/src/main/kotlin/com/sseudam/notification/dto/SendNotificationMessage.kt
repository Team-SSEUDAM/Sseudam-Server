package com.sseudam.notification.dto

data class SendNotificationMessage(
    val userId: Long,
    val title: String,
    val body: String,
    val destination: String, // ex) "sseudam://notification"
)
