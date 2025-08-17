package com.sseudam.client.notification

data class FcmSendResponse(
    val fcmToken: String,
    val title: String,
    val body: String,
    val success: Boolean,
)
