package com.sseudam.notification.component

import com.sseudam.notification.NotificationStored
import com.sseudam.notification.repository.NotificationStoredRepository
import com.sseudam.support.cursor.Cursor
import com.sseudam.support.cursor.CursorRequest
import org.springframework.stereotype.Component

@Component
class NotificationStoredReader(
    private val notificationStoredRepository: NotificationStoredRepository,
) {
    fun findAllBy(
        userId: Long,
        cursorRequest: CursorRequest,
    ): Cursor<NotificationStored.Info> = notificationStoredRepository.findAllBy(userId, cursorRequest)
}
