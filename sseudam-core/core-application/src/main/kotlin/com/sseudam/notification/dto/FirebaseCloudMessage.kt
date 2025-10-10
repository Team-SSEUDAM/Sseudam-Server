package com.sseudam.notification.dto

data class FirebaseCloudMessage(
    val fcmKey: FcmKey,
    val fcmToken: String,
    val title: String,
    val body: String,
    val destination: String, // ex) "sseudam://notification"
    val tryCount: Int,
    val sent: Boolean,
)
