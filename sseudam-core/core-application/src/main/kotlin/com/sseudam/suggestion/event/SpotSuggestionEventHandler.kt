package com.sseudam.suggestion.event

import com.sseudam.common.GeoJson
import com.sseudam.contract.suggestion.SuggestionUpdateEvent
import com.sseudam.notification.event.SuggestionDiscordNotificationRequestedEvent
import com.sseudam.notification.event.SuggestionFcmNotificationRequestedEvent
import com.sseudam.support.error.ErrorException
import com.sseudam.support.error.ErrorType
import com.sseudam.support.extension.logger
import com.sseudam.user.UserDeviceService
import com.sseudam.user.UserService
import org.springframework.context.ApplicationEventPublisher
import org.springframework.modulith.events.ApplicationModuleListener
import org.springframework.stereotype.Component

@Component
class SpotSuggestionEventHandler(
    private val userService: UserService,
    private val userDeviceService: UserDeviceService,
    private val eventPublisher: ApplicationEventPublisher,
) {
    companion object {
        private val log by logger()
    }

    @ApplicationModuleListener(id = "suggestion-update-fcm-notification")
    fun suggestionUpdateNotificationListener(event: SuggestionUpdateEvent) {
        try {
            val suggestion = event.suggestion
            val userProfile =
                userService.getProfile(suggestion.userId)
                    ?: throw ErrorException(ErrorType.NOT_FOUND_USER)
            val userDevices = userDeviceService.findAllByUserId(userProfile.id)
            val fcmToken = userDevices.lastOrNull()?.fcmToken ?: return

            val (body, type) =
                when (suggestion.status) {
                    "APPROVE" -> {
                        val approveBody = "${userProfile.nickname}님의 제보가 승인되어 ${event.rewardPoint}포인트를 받았어요!"
                        approveBody to "APPROVE_SUGGESTION"
                    }
                    "REJECT" -> {
                        val rejectBody = "${userProfile.nickname}님의 제보가 반려되었어요."
                        rejectBody to "REJECT_SUGGESTION"
                    }
                    else -> throw ErrorException(ErrorType.INVALID_UPDATE_SUGGESTION_STATUS)
                }

            eventPublisher.publishEvent(
                SuggestionFcmNotificationRequestedEvent(
                    userId = suggestion.userId,
                    title = "쓰담쓰담",
                    body = body,
                    destination = "MyPageView",
                    notificationType = type,
                    parameterValue = suggestion.id.toString(),
                    fcmToken = fcmToken,
                ),
            )
        } catch (e: Exception) {
            log.warn(e) { "Failed to send notification for user ${event.suggestion.userId}" }
        }
    }

    @ApplicationModuleListener(id = "spot-suggestion-discord-notification")
    fun handleDiscordNotification(event: SpotSuggestionCreatedEvent) {
        val userProfile =
            userService.getProfile(event.spotSuggestion.userId)
                ?: throw ErrorException(ErrorType.NOT_FOUND_USER)
        val suggestion = event.spotSuggestion

        val pointCoordinate =
            when (val point = suggestion.point) {
                is GeoJson.Point -> point.coordinates
                else -> emptyList()
            }
        val coordinateText =
            if (pointCoordinate.size >= 2) {
                "${pointCoordinate[0]}, ${pointCoordinate[1]} (경도, 위도)"
            } else {
                "좌표 정보 없음"
            }

        eventPublisher.publishEvent(
            SuggestionDiscordNotificationRequestedEvent(
                id = suggestion.id,
                site = suggestion.address.site,
                spotName = suggestion.spotName,
                trashType = suggestion.trashType.displayName,
                userId = suggestion.userId,
                nickname = userProfile.nickname,
                coordinateText = coordinateText,
                createdAt = suggestion.createdAt,
            ),
        )
    }
}
