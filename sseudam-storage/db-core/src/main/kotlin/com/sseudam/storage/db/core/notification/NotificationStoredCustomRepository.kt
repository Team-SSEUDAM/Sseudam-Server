package com.sseudam.storage.db.core.notification

import com.sseudam.notification.NotificationStored
import com.sseudam.notification.NotificationType
import com.sseudam.storage.db.core.support.JDSLExtensions
import com.sseudam.support.cursor.Cursor
import com.sseudam.support.cursor.CursorRequest
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Repository

@Repository
class NotificationStoredCustomRepository(
    private val notificationStoredJpaRepository: NotificationStoredJpaRepository,
) {
    companion object {
        private val EXCLUDED_TYPES =
            listOf(
                NotificationType.REGULAR,
                NotificationType.NEW_PET_SEASON,
            )
    }

    fun findAllBy(
        userId: Long,
        cursorRequest: CursorRequest,
    ): Cursor<NotificationStored.Info> {
        val pageable = PageRequest.ofSize(cursorRequest.size.toInt())
        val notifications =
            notificationStoredJpaRepository.findPage(JDSLExtensions, pageable) {
                select(entity(NotificationStoredEntity::class))
                    .from(entity(NotificationStoredEntity::class))
                    .whereAnd(
                        path(NotificationStoredEntity::userId).equal(userId),
                        path(NotificationStoredEntity::type).notIn(EXCLUDED_TYPES),
                        cursorRequest.lastId?.let {
                            path(NotificationStoredEntity::id).lessThan(cursorRequest.lastId)
                        },
                    ).orderBy(
                        path(NotificationStoredEntity::id).desc(),
                    )
            }
        val content =
            notifications.content
                .filterNotNull()
                .map { it.toNotificationStoredInfo() }
        val nextCursor = if (content.size < cursorRequest.size) null else notifications.lastOrNull()?.id

        return Cursor.of(
            nextCursor = nextCursor,
            size = notifications.totalElements,
            content = content,
        )
    }
}
