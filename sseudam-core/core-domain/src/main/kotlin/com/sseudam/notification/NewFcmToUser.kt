package com.sseudam.notification

data class NewFcmToUser(
    val userKey: String,
    val title: String,
    val body: String,
)
