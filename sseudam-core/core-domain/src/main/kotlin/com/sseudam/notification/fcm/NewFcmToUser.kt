package com.sseudam.notification.fcm

data class NewFcmToUser(
    val userKey: String,
    val title: String,
    val body: String,
)
