package com.sseudam.admin.presentation.v1.notification

import com.sseudam.admin.application.AdminFacade
import com.sseudam.admin.presentation.request.AdminPushNotificationRequest
import com.sseudam.admin.presentation.v1.annotation.AdminTagDocs
import com.sseudam.admin.presentation.v1.annotation.ApiAdminV1Controller
import io.swagger.v3.oas.annotations.Operation
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody

@AdminTagDocs
@ApiAdminV1Controller
class AdminNotificationController(
    private val adminFacade: AdminFacade,
) {
    @Operation(summary = "푸시 알림", description = "전체 사용자에게 푸시 알림을 보냅니다.")
    @PostMapping("/push-all")
    fun pushToAllUsers(
        @RequestBody request: AdminPushNotificationRequest,
    ) {
        adminFacade.pushToAllUsers(request.topic, request.contents)
    }
}
