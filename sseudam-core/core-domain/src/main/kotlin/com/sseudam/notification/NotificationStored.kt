package com.sseudam.notification

class NotificationStored {
    data class Create(
        val notificationStoredKey: String,
        val userId: Long,
        val type: String,
        val parameterValue: String,
        val topic: String,
        val contents: String,
        val readStatus: ReadStatus,
    )

    data class Info(
        val id: Long,
        val notificationStoredKey: String,
        val userId: Long,
        val type: String,
        val parameterValue: String,
        val topic: String,
        val contents: String,
        val readStatus: ReadStatus,
        val createdAt: String,
    )
}
