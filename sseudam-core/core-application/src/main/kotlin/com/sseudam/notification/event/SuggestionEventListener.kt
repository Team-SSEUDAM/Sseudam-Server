package com.sseudam.notification.event

import com.sseudam.notification.NotificationType
import com.sseudam.notification.discord.DiscordClient
import com.sseudam.notification.dto.SendMessageSuggestionDto
import com.sseudam.notification.dto.SendNotificationMessage
import com.sseudam.notification.fcm.FcmSender
import org.springframework.modulith.events.ApplicationModuleListener
import org.springframework.stereotype.Component

@Component
class SuggestionEventListener(
    private val fcmSender: FcmSender,
    private val discordClient: DiscordClient,
) {
    @ApplicationModuleListener
    fun handleSuggestionFcmNotificationRequested(event: SuggestionFcmNotificationRequestedEvent) {
        fcmSender.send(
            sendNotificationMessage =
                SendNotificationMessage(
                    userId = event.userId,
                    title = event.title,
                    body = event.body,
                    destination = event.destination,
                ),
            type = NotificationType.valueOf(event.notificationType),
            parameterValue = event.parameterValue,
        )
    }

    @ApplicationModuleListener
    fun handleSuggestionDiscordNotificationRequested(event: SuggestionDiscordNotificationRequestedEvent) {
        discordClient.sendSuggestionMessage(
            SendMessageSuggestionDto(
                id = event.id,
                site = event.site,
                spotName = event.spotName,
                trashType = event.trashType,
                userId = event.userId,
                nickname = event.nickname,
                coordinateText = event.coordinateText,
                createdAt = event.createdAt,
            ),
        )
    }
}
