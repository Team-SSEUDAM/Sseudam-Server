package com.sseudam.client.notification

data class FcmSendRequest(
    val fcmToken: String,
    val title: String,
    val body: String,
    val destination: String, // ex) "sseudam://notification"
)
