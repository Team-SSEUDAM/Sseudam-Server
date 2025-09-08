package com.sseudam.suggestion

import com.sseudam.notification.FcmSender
import com.sseudam.notification.NotificationMessages
import com.sseudam.notification.SendNotificationMessage
import com.sseudam.suggestion.event.SuggestionUpdateEvent
import com.sseudam.support.error.ErrorException
import com.sseudam.support.error.ErrorType
import com.sseudam.support.extension.logger
import com.sseudam.user.UserService
import org.springframework.modulith.events.ApplicationModuleListener
import org.springframework.stereotype.Component

@Component
class SuggestionEventListener(
    private val userService: UserService,
    private val fcmSender: FcmSender,
) {
    companion object {
        private val log by logger()
    }

    @ApplicationModuleListener
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
}
