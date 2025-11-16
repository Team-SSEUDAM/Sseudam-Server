package com.sseudam.notification.internal

import com.sseudam.notification.NotificationType
import com.sseudam.notification.discord.DiscordClient
import com.sseudam.notification.dto.SendMessageReportDto
import com.sseudam.notification.dto.SendMessageSuggestionDto
import com.sseudam.notification.dto.SendMessageUserProfileDto
import com.sseudam.notification.dto.SendNotificationMessage
import com.sseudam.notification.event.ReportDiscordNotificationRequestedEvent
import com.sseudam.notification.event.ReportFcmNotificationRequestedEvent
import com.sseudam.notification.event.SuggestionDiscordNotificationRequestedEvent
import com.sseudam.notification.event.SuggestionFcmNotificationRequestedEvent
import com.sseudam.notification.event.UserDiscordNotificationRequestedEvent
import com.sseudam.notification.fcm.FcmSender
import org.springframework.modulith.events.ApplicationModuleListener
import org.springframework.stereotype.Component

/**
 * Internal event handlers for notifications.
 * These are internal implementation details and not part of the notification module's public API.
 */
@Component
internal class NotificationEventHandlers(
    private val fcmSender: FcmSender,
    private val discordClient: DiscordClient,
) {
    @ApplicationModuleListener
    fun handleReportFcmNotification(event: ReportFcmNotificationRequestedEvent) {
        if (event.fcmToken.isBlank()) return

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
            fcmToken = event.fcmToken,
        )
    }

    @ApplicationModuleListener
    fun handleReportDiscordNotification(event: ReportDiscordNotificationRequestedEvent) {
        discordClient.sendReportMessage(
            SendMessageReportDto(
                id = event.id,
                userId = event.userId,
                nickname = event.nickname,
                reportType = event.reportType,
                reportBody = event.reportBody,
                createdAt = event.createdAt,
            ),
        )
    }

    @ApplicationModuleListener
    fun handleSuggestionFcmNotification(event: SuggestionFcmNotificationRequestedEvent) {
        if (event.fcmToken.isBlank()) return

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
            fcmToken = event.fcmToken,
        )
    }

    @ApplicationModuleListener
    fun handleSuggestionDiscordNotification(event: SuggestionDiscordNotificationRequestedEvent) {
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

    @ApplicationModuleListener
    fun handleUserDiscordNotification(event: UserDiscordNotificationRequestedEvent) {
        discordClient.sendCreateUserMessage(
            SendMessageUserProfileDto(
                id = event.id,
                email = event.email,
                nickname = event.nickname,
                site = event.site,
                createdAt = event.createdAt,
            ),
        )
    }
}
