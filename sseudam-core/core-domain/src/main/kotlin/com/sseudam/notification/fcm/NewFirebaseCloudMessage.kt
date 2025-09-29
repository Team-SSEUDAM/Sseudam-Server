package com.sseudam.notification.fcm

data class NewFirebaseCloudMessage(
    val fcmToken: String,
    val title: String,
    val body: String,
)
