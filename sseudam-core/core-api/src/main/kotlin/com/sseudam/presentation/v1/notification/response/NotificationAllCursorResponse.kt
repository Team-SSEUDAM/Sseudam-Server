package com.sseudam.presentation.v1.notification.response

import com.sseudam.notification.NotificationStored
import com.sseudam.support.cursor.Cursor

data class NotificationAllCursorResponse(
    val list: List<NotificationStored.Info>,
    val nextCursor: Long?,
    val size: Long,
) {
    companion object {
        fun from(cursor: Cursor<NotificationStored.Info>) =
            NotificationAllCursorResponse(
                list = cursor.content,
                nextCursor = cursor.nextCursor,
                size = cursor.size,
            )
    }
}
