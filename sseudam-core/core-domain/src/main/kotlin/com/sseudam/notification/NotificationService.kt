package com.sseudam.notification

import org.springframework.stereotype.Service

@Service
class NotificationService(
    private val notificationStoredAppender: NotificationStoredAppender,
    private val notificationStoredKeyGenerator: NotificationStoredKeyGenerator,
) {
    fun append(notificationStored: NotificationStored.Create): NotificationStored.Info =
        notificationStoredAppender.append(
            notificationStored.copy(
                notificationStoredKey = notificationStoredKeyGenerator.generate(),
            ),
        )

    fun appendAll(createAll: List<NotificationStored.Create>): List<NotificationStored.Info> =
        notificationStoredAppender.appendAll(
            createAll.map { notification ->
                notification.copy(
                    notificationStoredKey = notificationStoredKeyGenerator.generate(),
                )
            },
        )
}
