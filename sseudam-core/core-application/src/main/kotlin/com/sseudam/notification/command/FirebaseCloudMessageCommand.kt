package com.sseudam.notification.command

data class FirebaseCloudMessageCommand(
    val fcmToken: String,
    val title: String,
    val body: String,
    val destination: String, // ex) "sseudam://notification"
)
