package com.sseudam.notification

import org.springframework.stereotype.Component

@Component
class NotificationStoredAppender(
    private val notificationStoredRepository: NotificationStoredRepository,
) {
    fun appendAll(createAll: List<NotificationStored.Create>) {
    }
}
