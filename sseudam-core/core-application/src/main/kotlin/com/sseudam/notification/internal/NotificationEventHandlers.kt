package com.sseudam.notification.internal

import com.sseudam.notification.NotificationType
import com.sseudam.notification.discord.DiscordClient
import com.sseudam.notification.dto.SendMessageReportDto
import com.sseudam.notification.dto.SendMessageSuggestionDto
import com.sseudam.notification.dto.SendMessageUserProfileDto
import com.sseudam.notification.dto.SendNotificationMessage
import com.sseudam.notification.event.NewPetNotificationRequestedEvent
import com.sseudam.notification.event.ReportDiscordNotificationRequestedEvent
import com.sseudam.notification.event.ReportFcmNotificationRequestedEvent
import com.sseudam.notification.event.SuggestionDiscordNotificationRequestedEvent
import com.sseudam.notification.event.SuggestionFcmNotificationRequestedEvent
import com.sseudam.notification.event.UserDiscordNotificationRequestedEvent
import com.sseudam.notification.fcm.FcmSender
import com.sseudam.user.UserDeviceService
import com.sseudam.user.UserService
import com.sseudam.visit.event.VisitedSpotNotificationRequestedEvent
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
    private val userDeviceService: UserDeviceService,
    private val userService: UserService,
) {
    @ApplicationModuleListener
    fun handleReportFcmNotification(event: ReportFcmNotificationRequestedEvent) {
        val userDevice = userDeviceService.findByUserId(event.userId) ?: return
        val fcmToken = userDevice.fcmToken

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
            fcmToken = fcmToken,
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
        val userDevices = userDeviceService.findAllByUserId(event.userId)
        val fcmToken = userDevices.lastOrNull()?.fcmToken ?: return

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
            fcmToken = fcmToken,
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
        discordClient.sendUserMessage(
            SendMessageUserProfileDto(
                id = event.id,
                email = event.email,
                nickname = event.nickname,
                site = event.site,
                createdAt = event.createdAt,
            ),
        )
    }

    @ApplicationModuleListener
    fun handleVisitedSpotNotification(event: VisitedSpotNotificationRequestedEvent) {
        val suggesterId = event.suggesterId ?: return
        val userDevices = userDeviceService.findAllByUserId(suggesterId)
        val fcmToken = userDevices.lastOrNull()?.fcmToken ?: return

        fcmSender.send(
            sendNotificationMessage =
                SendNotificationMessage(
                    userId = suggesterId,
                    title = event.title,
                    body = event.body,
                    destination = event.destination,
                ),
            type = NotificationType.valueOf(event.notificationType),
            parameterValue = event.parameterValue,
            fcmToken = fcmToken,
        )
    }

    @ApplicationModuleListener
    fun handleNewPetNotification(event: NewPetNotificationRequestedEvent) {
        val allDevices =
            userDeviceService
                .findAll()
                .sortedByDescending { it.createdAt }
                .filter { it.fcmToken.isNotBlank() }
                .distinctBy { it.userId }

        if (allDevices.isEmpty()) return

        val userProfiles =
            userService
                .findAllBy(allDevices.map { it.userId }.distinct())
                .associateBy { it.id }

        allDevices.forEach { device ->
            fcmSender.send(
                sendNotificationMessage =
                    SendNotificationMessage(
                        userId = device.userId,
                        title = "새로운 펫이 도착했습니다!",
                        body = "${userProfiles[device.userId]?.nickname ?: "사용자"}님의 새로운 펫을 확인하세요!",
                        destination = "MyPetView",
                    ),
                type = NotificationType.NEW_PET_SEASON,
                parameterValue = "",
                fcmToken = device.fcmToken,
            )
        }
    }
}
