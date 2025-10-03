package com.sseudam.notification.command

data class FcmToUserCommand(
    val userKey: String,
    val title: String,
    val body: String,
)
