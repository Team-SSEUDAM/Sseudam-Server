package com.sseudam.report.event

import com.sseudam.notification.NotificationType
import com.sseudam.notification.discord.DiscordClient
import com.sseudam.notification.dto.NotificationMessages
import com.sseudam.notification.dto.SendNotificationMessage
import com.sseudam.notification.fcm.FcmSender
import com.sseudam.report.ReportStatus
import com.sseudam.report.ReportType
import com.sseudam.report.dto.SendMessageReportDto
import com.sseudam.support.error.ErrorException
import com.sseudam.support.error.ErrorType
import com.sseudam.support.extension.logger
import com.sseudam.trashspot.TrashSpotService
import com.sseudam.trashspot.image.TrashSpotImageService
import com.sseudam.user.UserService
import org.springframework.modulith.events.ApplicationModuleListener
import org.springframework.stereotype.Component

@Component
class SpotReportEventHandler(
    private val trashSpotService: TrashSpotService,
    private val trashSpotImageService: TrashSpotImageService,
    private val userService: UserService,
    private val fcmSender: FcmSender,
    private val discordClient: DiscordClient,
) {
    companion object {
        private val log by logger()
    }

    @ApplicationModuleListener(id = "update-report-trash-spot")
    fun updateReportListener(event: SpotReportUpdateEvent) {
        when (event.report.reportType) {
            ReportType.PHOTO -> {
                trashSpotImageService.updateImage(
                    event.report.spotId,
                    event.report.imageUrl,
                )
            }
            else -> {
                trashSpotService.updateByReport(event.report)
            }
        }
    }

    @ApplicationModuleListener(id = "report-update-fcm-notification")
    fun reportUpdateNotificationListener(event: SpotReportUpdateEvent) {
        try {
            val userId = event.report.userId
            val targetId = event.report.id
            val userProfile = userService.getProfile(userId) ?: return

            val (body, type) =
                when (event.report.status) {
                    ReportStatus.APPROVE ->
                        NotificationMessages.approveReportContents(userProfile.nickname) to NotificationType.APPROVE_REPORT
                    ReportStatus.REJECT ->
                        NotificationMessages.rejectReportContents(userProfile.nickname) to NotificationType.REJECT_REPORT
                    else -> throw IllegalArgumentException("Invalid report status: ${event.report.status}")
                }

            fcmSender.send(
                sendNotificationMessage =
                    SendNotificationMessage(
                        userId = userId,
                        title = NotificationMessages.DEFAULT_TITLE,
                        body = body,
                        destination = "notifications",
                    ),
                type = type,
                parameterValue = targetId.toString(),
            )
        } catch (e: Exception) {
            log.warn(e) { "Failed to send notification for user ${event.report.userId}" }
        }
    }

    @ApplicationModuleListener(id = "spot-report-discord-notification")
    fun handleDiscordNotification(event: SpotReportCreatedEvent) {
        val userProfile =
            userService.getProfile(event.spotReport.userId)
                ?: throw ErrorException(ErrorType.NOT_FOUND_USER)
        discordClient.sendReportMessage(
            SendMessageReportDto.of(
                event.spotReport,
                userProfile,
            ),
        )
    }
}
