package com.sseudam.presentation.v1.notification.response

import com.sseudam.notification.NotificationStored
import com.sseudam.support.cursor.Cursor
import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "알림함 리스트 응답(커서 페이지네이션)")
data class NotificationAllCursorResponse(
    @Schema(description = "알림함 리스트")
    val list: List<NotificationResponse>,
    @Schema(description = "다음 커서 ID")
    val nextCursor: Long?,
) {
    companion object {
        fun from(cursor: Cursor<NotificationStored.Info>) =
            NotificationAllCursorResponse(
                list = cursor.content.map { NotificationResponse.from(it) },
                nextCursor = cursor.nextCursor,
            )
    }
}
