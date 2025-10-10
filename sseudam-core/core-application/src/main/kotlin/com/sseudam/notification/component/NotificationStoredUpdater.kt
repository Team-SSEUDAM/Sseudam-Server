package com.sseudam.notification.component

import com.sseudam.notification.repository.NotificationStoredRepository
import org.springframework.stereotype.Component

@Component
class NotificationStoredUpdater(
    private val notificationStoredRepository: NotificationStoredRepository,
) {
    fun markAsRead(
        userId: Long,
        notificationId: Long,
    ) = notificationStoredRepository.markAsRead(notificationId)
}
