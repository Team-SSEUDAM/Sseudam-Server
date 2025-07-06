package com.sseudam.presentation.v1.user.request

import com.sseudam.user.User
import com.sseudam.user.device.UserDevice
import io.swagger.v3.oas.annotations.media.Schema
import java.util.UUID

@Schema(description = "FCM 토큰 등록 요청 Json")
data class UserMobileDeviceRequest(
    @Schema(description = "FCM 토큰", example = "fcm_token")
    val fcmToken: String,
) {
    fun toCreate(
        user: User,
        deviceId: String?,
    ): UserDevice.Create =
        UserDevice.Create(
            userId = user.id,
            userKey = user.key,
            fcmToken = fcmToken,
            deviceId = deviceId ?: UUID.randomUUID().toString(),
        )
}
