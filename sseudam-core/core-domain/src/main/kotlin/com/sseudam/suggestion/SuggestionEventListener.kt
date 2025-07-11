package com.sseudam.suggestion

import com.sseudam.notification.FcmSender
import com.sseudam.notification.NotificationMessages
import com.sseudam.notification.SendNotificationMessage
import com.sseudam.suggestion.event.SuggestionUpdateEvent
import com.sseudam.support.error.ErrorException
import com.sseudam.support.error.ErrorType
import com.sseudam.support.extension.logger
import com.sseudam.trashspot.TrashSpotService
import com.sseudam.trashspot.image.TrashSpotImage
import com.sseudam.trashspot.image.TrashSpotImageService
import com.sseudam.user.UserService
import org.springframework.modulith.events.ApplicationModuleListener
import org.springframework.stereotype.Component

@Component
class SuggestionEventListener(
    private val trashSpotService: TrashSpotService,
    private val trashSpotImageService: TrashSpotImageService,
    private val userService: UserService,
    private val fcmSender: FcmSender,
) {
    companion object {
        private val log by logger()
    }

    @ApplicationModuleListener
    fun createSuggestionListener(event: SuggestionUpdateEvent) {
        if (event.suggestion.status != SuggestionStatus.APPROVE) return
        val trashSpot = trashSpotService.createTrashSpotBySuggestion(event.suggestion)
        trashSpotImageService.append(
            TrashSpotImage.Create(
                trashSpot.id,
                event.suggestion.imageUrl,
            ),
        )
    }

    @ApplicationModuleListener
    fun suggestionUpdateNotificationListener(event: SuggestionUpdateEvent) {
        try {
            val userId = event.suggestion.userId
            val type = "SUGGESTION"
            val targetId = event.suggestion.id
            val body =
                when (event.suggestion.status) {
                    SuggestionStatus.APPROVE -> NotificationMessages.APPROVE_SUGGESTION_CONTENTS
                    SuggestionStatus.REJECT -> NotificationMessages.REJECT_SUGGESTION_CONTENTS
                    else -> throw ErrorException(ErrorType.INVALID_UPDATE_SUGGESTION_STATUS)
                }
            val userProfile = userService.getProfile(userId)

            fcmSender.send(
                sendNotificationMessage =
                    SendNotificationMessage(
                        userId = userId,
                        title = NotificationMessages.DEFAULT_TITLE,
                        body = userProfile.nickname + body,
                    ),
                type = type,
                parameterValue = targetId.toString(),
            )
        } catch (e: Exception) {
            log.warn("Failed to send notification for user ${event.suggestion.userId}", e)
        }
    }
}
