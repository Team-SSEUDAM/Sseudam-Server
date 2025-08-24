package com.sseudam.report

import com.sseudam.notification.FcmSender
import com.sseudam.notification.NotificationMessages
import com.sseudam.notification.SendNotificationMessage
import com.sseudam.report.event.ReportUpdateEvent
import com.sseudam.support.error.ErrorException
import com.sseudam.support.error.ErrorType
import com.sseudam.support.extension.logger
import com.sseudam.trashspot.TrashSpotService
import com.sseudam.trashspot.image.TrashSpotImageService
import com.sseudam.user.UserService
import org.springframework.modulith.events.ApplicationModuleListener
import org.springframework.stereotype.Component

@Component
class ReportEventListener(
    private val trashSpotService: TrashSpotService,
    private val trashSpotImageService: TrashSpotImageService,
    private val userService: UserService,
    private val fcmSender: FcmSender,
) {
    companion object {
        private val log by logger()
    }

    @ApplicationModuleListener
    fun updateReportListener(event: ReportUpdateEvent) {
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

    @ApplicationModuleListener
    fun reportUpdateNotificationListener(event: ReportUpdateEvent) {
        try {
            val userId = event.report.userId
            val type = "REPORT"
            val targetId = event.report.id
            val body =
                when (event.report.status) {
                    ReportStatus.APPROVE -> NotificationMessages.APPROVE_REPORT_CONTENTS
                    ReportStatus.REJECT -> NotificationMessages.REJECT_REPORT_CONTENTS
                    else -> throw IllegalArgumentException("Invalid report status: ${event.report.status}")
                }
            val userProfile = userService.getProfile(userId) ?: throw ErrorException(ErrorType.NOT_FOUND_USER)

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
            log.warn(e) { "Failed to send notification for user ${event.report.userId}" }
        }
    }
}
