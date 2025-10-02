package com.sseudam.suggestion.event

import com.sseudam.notification.discord.DiscordClient
import com.sseudam.notification.dto.NotificationMessages
import com.sseudam.notification.dto.SendNotificationMessage
import com.sseudam.notification.fcm.FcmSender
import com.sseudam.suggestion.SuggestionStatus
import com.sseudam.support.error.ErrorException
import com.sseudam.support.error.ErrorType
import com.sseudam.support.extension.logger
import com.sseudam.user.UserService
import org.springframework.modulith.events.ApplicationModuleListener
import org.springframework.stereotype.Component

@Component
class SpotSuggestionEventHandler(
    private val userService: UserService,
    private val fcmSender: FcmSender,
    private val discordClient: DiscordClient,
) {
    companion object {
        private val log by logger()
    }

    @ApplicationModuleListener(id = "suggestion-update-fcm-notification")
    fun suggestionUpdateNotificationListener(event: SuggestionUpdateEvent) {
        try {
            val suggestion = event.suggestion
            val body =
                when (suggestion.status) {
                    SuggestionStatus.APPROVE -> NotificationMessages.APPROVE_SUGGESTION_CONTENTS
                    SuggestionStatus.REJECT -> NotificationMessages.REJECT_SUGGESTION_CONTENTS
                    else -> throw ErrorException(ErrorType.INVALID_UPDATE_SUGGESTION_STATUS)
                }
            val userProfile =
                userService.getProfile(suggestion.userId)
                    ?: throw ErrorException(ErrorType.NOT_FOUND_USER)

            fcmSender.send(
                SendNotificationMessage(
                    userId = suggestion.userId,
                    title = NotificationMessages.DEFAULT_TITLE,
                    body = userProfile.nickname + body,
                ),
                type = "SUGGESTION",
                parameterValue = suggestion.id.toString(),
            )
        } catch (e: Exception) {
            log.warn(e) { "Failed to send notification for user ${event.suggestion.userId}" }
        }
    }

    @ApplicationModuleListener(id = "spot-suggestion-discord-notification")
    fun handleDiscordNotification(event: SpotSuggestionCreatedEvent) {
        discordClient.sendSuggestionMessage(event.spotSuggestion)
    }
}
