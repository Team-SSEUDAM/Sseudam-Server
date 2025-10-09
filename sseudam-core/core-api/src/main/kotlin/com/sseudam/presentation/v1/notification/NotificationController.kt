package com.sseudam.presentation.v1.notification

import com.sseudam.notification.NotificationFacade
import com.sseudam.notification.NotificationService
import com.sseudam.presentation.v1.annotation.ApiV1Controller
import com.sseudam.presentation.v1.notification.response.NotificationAllCursorResponse
import com.sseudam.support.cursor.CursorRequest
import com.sseudam.user.User
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam

@Tag(name = "🔔 Notification API", description = "알림 관련 API")
@ApiV1Controller
class NotificationController(
    private val notificationService: NotificationService,
    private val notificationFacade: NotificationFacade,
) {
    @Operation(summary = "알림 조회", description = "사용자의 알림 목록을 조회합니다.")
    @GetMapping("/notifications")
    fun getNotifications(
        user: User,
        @RequestParam size: Long,
        @RequestParam lastId: Long?,
    ): NotificationAllCursorResponse {
        val notifications = notificationFacade.getNotifications(user.id, CursorRequest(size = size, lastId = lastId))
        return NotificationAllCursorResponse.from(notifications)
    }
}
