package com.sseudam.report.event

import com.sseudam.common.GeoJson
import com.sseudam.notification.event.ReportDiscordNotificationRequestedEvent
import com.sseudam.notification.event.ReportFcmNotificationRequestedEvent
import com.sseudam.report.ReportStatus
import com.sseudam.report.ReportType
import com.sseudam.report.strategy.ReportTypeStrategyProvider
import com.sseudam.support.error.ErrorException
import com.sseudam.support.error.ErrorType
import com.sseudam.support.extension.logger
import com.sseudam.user.UserDeviceService
import com.sseudam.user.UserService
import org.springframework.context.ApplicationEventPublisher
import org.springframework.modulith.events.ApplicationModuleListener
import org.springframework.stereotype.Component

@Component
class SpotReportEventHandler(
    private val reportTypeStrategyProvider: MutableList<ReportTypeStrategyProvider>,
    private val userService: UserService,
    private val userDeviceService: UserDeviceService,
    private val eventPublisher: ApplicationEventPublisher,
) {
    companion object {
        private val log by logger()
    }

    @ApplicationModuleListener(id = "update-report-trash-spot")
    fun updateReportListener(event: SpotReportUpdateEvent) {
        routingReportTypeStrategyProvider(event.report.reportType)
            .update(report = event.report)
    }

    @ApplicationModuleListener(id = "report-update-fcm-notification")
    fun reportUpdateNotificationListener(event: SpotReportUpdateEvent) {
        try {
            val userId = event.report.userId
            val targetId = event.report.id
            val userProfile = userService.getProfile(userId) ?: return
            val userDevices = userDeviceService.findAllByUserId(userId)
            val fcmToken = userDevices.lastOrNull()?.fcmToken ?: return

            val (body, type) =
                when (event.report.status) {
                    ReportStatus.APPROVE -> {
                        val approveBody = "${userProfile.nickname}님의 신고가 승인되어 ${event.rewardPoint}포인트를 받았어요!"
                        approveBody to "APPROVE_REPORT"
                    }
                    ReportStatus.REJECT -> {
                        val rejectBody = "${userProfile.nickname}님의 신고가 반려되었어요."
                        rejectBody to "REJECT_REPORT"
                    }
                    else -> throw IllegalArgumentException("Invalid report status: ${event.report.status}")
                }

            eventPublisher.publishEvent(
                ReportFcmNotificationRequestedEvent(
                    userId = userId,
                    title = "쓰담쓰담",
                    body = body,
                    destination = "MyPageView",
                    notificationType = type,
                    parameterValue = targetId.toString(),
                    fcmToken = fcmToken,
                ),
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
        val report = event.spotReport

        val reportBody = "**신고 내용:** ${
            when (report.reportType) {
                ReportType.POINT -> {
                    when (val point = report.point) {
                        is GeoJson.Point -> {
                            val coords = point.coordinates
                            if (coords.size >= 2) {
                                "${coords[0]}, ${coords[1]} (경도, 위도)"
                            } else {
                                "좌표 정보 없음"
                            }
                        }
                        else -> "좌표 정보 없음"
                    }
                }
                ReportType.NAME -> report.spotName
                ReportType.KIND -> report.trashType.displayName
                ReportType.PHOTO -> report.imageUrl
                ReportType.EMPTY_SPOT -> "잘못된 쓰레기통 장소"
                ReportType.ETC -> "기타 사유"
            }
        } 로 수정 요청"

        eventPublisher.publishEvent(
            ReportDiscordNotificationRequestedEvent(
                id = report.id,
                userId = report.userId,
                nickname = userProfile.nickname,
                reportType = report.reportType.displayName,
                reportBody = reportBody,
                createdAt = report.createdAt,
            ),
        )
    }

    private fun routingReportTypeStrategyProvider(reportType: ReportType): ReportTypeStrategyProvider =
        reportTypeStrategyProvider.firstOrNull {
            it.supports(reportType)
        } ?: throw ErrorException(ErrorType.NOT_FOUND_DATA, reportType.displayName)
}
