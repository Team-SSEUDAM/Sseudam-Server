package com.sseudam.suggestion.event

import com.sseudam.notification.NotificationType
import com.sseudam.notification.discord.DiscordClient
import com.sseudam.notification.dto.NotificationMessages
import com.sseudam.notification.dto.SendNotificationMessage
import com.sseudam.notification.fcm.FcmSender
import com.sseudam.suggestion.SuggestionStatus
import com.sseudam.suggestion.dto.SendMessageSuggestionDto
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
            val userProfile =
                userService.getProfile(suggestion.userId)
                    ?: throw ErrorException(ErrorType.NOT_FOUND_USER)
            val (body, type) =
                when (suggestion.status) {
                    SuggestionStatus.APPROVE ->
                        NotificationMessages.approveSuggestionContents(userProfile.nickname) to
                            NotificationType.APPROVE_SUGGESTION
                    SuggestionStatus.REJECT ->
                        NotificationMessages.rejectSuggestionContents(userProfile.nickname) to
                            NotificationType.REJECT_SUGGESTION
                    else -> throw ErrorException(ErrorType.INVALID_UPDATE_SUGGESTION_STATUS)
                }

            fcmSender.send(
                SendNotificationMessage(
                    userId = suggestion.userId,
                    title = NotificationMessages.DEFAULT_TITLE,
                    body = body,
                ),
                type = type,
                parameterValue = suggestion.id.toString(),
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
        discordClient.sendSuggestionMessage(
            SendMessageSuggestionDto.of(
                event.spotSuggestion,
                userProfile,
            ),
        )
    }
}
