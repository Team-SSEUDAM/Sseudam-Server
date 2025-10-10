package com.sseudam.presentation.v1.notification

import com.sseudam.notification.NotificationFacade
import com.sseudam.presentation.v1.annotation.ApiV1Controller
import com.sseudam.presentation.v1.notification.response.NotificationAllCursorResponse
import com.sseudam.presentation.v1.notification.response.NotificationMessageResponse
import com.sseudam.support.cursor.CursorRequest
import com.sseudam.user.User
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestParam

@Tag(name = "🔔 Notification API", description = "알림 관련 API")
@ApiV1Controller
class NotificationController(
    private val notificationFacade: NotificationFacade,
) {
    @Operation(summary = "알림 조회", description = "사용자의 알림 목록을 조회합니다.")
    @GetMapping("/notifications")
    fun getNotifications(
        user: User,
        @RequestParam size: Long,
        @RequestParam lastId: Long?,
    ): NotificationAllCursorResponse =
        NotificationAllCursorResponse.from(
            notificationFacade.getNotifications(user.id, CursorRequest(size = size, lastId = lastId)),
        )

    @Operation(summary = "알림 읽음 처리", description = "사용자의 알림을 읽음 처리합니다.")
    @PutMapping("/notifications/{notificationId}/read")
    fun markNotificationAsRead(
        user: User,
        @PathVariable notificationId: Long,
    ): NotificationMessageResponse {
        notificationFacade.markNotificationAsRead(user.id, notificationId)
        return NotificationMessageResponse("알림이 읽음 처리되었습니다.")
    }
}
