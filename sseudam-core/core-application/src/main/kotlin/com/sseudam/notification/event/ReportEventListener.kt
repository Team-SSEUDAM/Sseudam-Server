package com.sseudam.notification.event

import com.sseudam.notification.NotificationType
import com.sseudam.notification.discord.DiscordClient
import com.sseudam.notification.dto.SendMessageReportDto
import com.sseudam.notification.dto.SendNotificationMessage
import com.sseudam.notification.fcm.FcmSender
import org.springframework.modulith.events.ApplicationModuleListener
import org.springframework.stereotype.Component

@Component
class ReportEventListener(
    private val fcmSender: FcmSender,
    private val discordClient: DiscordClient,
) {
    @ApplicationModuleListener
    fun handleReportFcmNotificationRequested(event: ReportFcmNotificationRequestedEvent) {
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
    fun handleReportDiscordNotificationRequested(event: ReportDiscordNotificationRequestedEvent) {
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
}
