package com.sseudam.notification

import com.sseudam.notification.command.CreateNotificationStoredCommand
import com.sseudam.notification.component.NotificationStoredAppender
import com.sseudam.notification.component.NotificationStoredKeyGenerator
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

    fun appendAll(createAll: List<CreateNotificationStoredCommand>): List<NotificationStored.Info> =
        notificationStoredAppender.appendAll(
            createAll.map { notification ->
                NotificationStored.Create(
                    notificationStoredKey = notificationStoredKeyGenerator.generate(),
                    userId = notification.userId,
                    type = notification.type,
                    parameterValue = notification.parameterValue,
                    topic = notification.topic,
                    contents = notification.contents,
                    readStatus = ReadStatus.UNREAD,
                )
            },
        )
}
