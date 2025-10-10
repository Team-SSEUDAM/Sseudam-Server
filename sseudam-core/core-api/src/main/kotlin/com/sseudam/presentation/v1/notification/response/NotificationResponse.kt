package com.sseudam.presentation.v1.notification.response

import com.sseudam.notification.NotificationStored
import com.sseudam.notification.NotificationType
import com.sseudam.notification.ReadStatus
import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "알림 응답")
data class NotificationResponse(
    @Schema(description = "알림 ID", example = "1")
    val id: Long,
    @Schema(description = "알림 저장된 키", example = "1")
    val notificationStoredKey: String,
    @Schema(description = "사용자 ID", example = "1")
    val userId: Long,
    @Schema(description = "알림 타입", example = "APPROVE_SUGGESTION")
    val type: NotificationType,
    @Schema(description = "알림 파라미터 값", example = "1")
    val parameterValue: Long,
    @Schema(description = "알림 토픽", example = "쓰담")
    val topic: String,
    @Schema(description = "알림 내용", example = "누군가 쓰담님이 제보한 쓰레기통에 쓰레기를 버렸어요! 지금 확인해보러 갈까요?")
    val contents: String,
    @Schema(description = "읽음 상태", example = "UNREAD")
    val readStatus: ReadStatus,
    @Schema(description = "생성일", example = "2025-10-08T17:15:28.001012")
    val createdAt: String,
) {
    companion object {
        fun from(info: NotificationStored.Info) =
            NotificationResponse(
                id = info.id,
                notificationStoredKey = info.notificationStoredKey,
                userId = info.userId,
                type = info.type,
                parameterValue = if (info.parameterValue.isNotEmpty()) info.parameterValue.toLong() else 0L,
                topic = info.topic,
                contents = info.contents,
                readStatus = info.readStatus,
                createdAt = info.createdAt,
            )
    }
}
