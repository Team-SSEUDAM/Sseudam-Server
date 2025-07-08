package com.sseudam.admin.application

import com.sseudam.admin.domain.AdminToken
import com.sseudam.admin.domain.AdminUserProfile
import com.sseudam.auth.AuthenticationService
import com.sseudam.auth.token.RefreshToken
import com.sseudam.notification.FcmSender
import com.sseudam.notification.NotificationMessages
import com.sseudam.notification.SendNotificationMessage
import com.sseudam.report.ReportService
import com.sseudam.report.ReportStatus
import com.sseudam.report.ReportType
import com.sseudam.report.SpotReport
import com.sseudam.report.UpdateReport
import com.sseudam.suggestion.SpotSuggestion
import com.sseudam.suggestion.SuggestionService
import com.sseudam.suggestion.SuggestionStatus
import com.sseudam.support.cursor.OffsetPageRequest
import com.sseudam.support.error.ErrorException
import com.sseudam.support.error.ErrorType
import com.sseudam.support.extension.logger
import com.sseudam.support.page.Page
import com.sseudam.trashspot.TrashSpotService
import com.sseudam.user.UserProfile
import com.sseudam.user.UserService
import com.sseudam.user.device.UserDeviceService
import com.sseudam.visit.SpotVisitedService
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class AdminFacade(
    private val adminService: AdminService,
    private val userService: UserService,
    private val userDeviceService: UserDeviceService,
    private val authService: AuthenticationService,
    private val suggestionService: SuggestionService,
    private val reportService: ReportService,
    private val spotVisitedService: SpotVisitedService,
    private val trashSpotService: TrashSpotService,
    private val fcmSender: FcmSender,
    private val passwordEncoder: PasswordEncoder,
) {
    companion object {
        private val log by logger()
    }

    fun login(
        loginId: String,
        password: String,
    ): AdminToken {
        val admin = adminService.findAdminUser(loginId)

        if (!passwordEncoder.matches(password, admin.password)) {
            throw ErrorException(ErrorType.INVALID_PASSWORD)
        }

        val token = authService.adminLogin(admin.id)
        return AdminToken(token.accessToken, token.refreshToken)
    }

    fun logout(accessToken: String) = authService.adminLogout(accessToken)

    fun reissue(refreshToken: RefreshToken): AdminToken {
        val token = authService.adminReissue(refreshToken)
        return AdminToken(token.accessToken, token.refreshToken)
    }

    fun findByUser(userId: Long): AdminUserProfile {
        val profile = userService.getProfile(userId)
        val visitedByUser = spotVisitedService.findAllByUser(userId)

        if (visitedByUser.isEmpty()) return AdminUserProfile.of(profile, emptyList())

        val spotIds = visitedByUser.map { it.spotId }
        val trashSpots = trashSpotService.findAllByIds(spotIds)

        return AdminUserProfile.of(profile, trashSpots)
    }

    fun findUsers(offsetPageRequest: OffsetPageRequest): Page<UserProfile> = userService.findUserProfileBy(offsetPageRequest)

    fun findSuggestions(
        offsetPageRequest: OffsetPageRequest,
        searchStatus: SuggestionStatus?,
    ): Page<SpotSuggestion.Info> = suggestionService.findSuggestionsBy(offsetPageRequest, searchStatus)

    fun findSuggestionDetails(suggestionId: Long): SpotSuggestion.Info = suggestionService.findSpotSuggestionById(suggestionId)

    fun findReports(
        offsetPageRequest: OffsetPageRequest,
        searchType: ReportType?,
    ): Page<SpotReport.Info> = reportService.findReportsBy(offsetPageRequest, searchType)

    fun findReportDetails(reportId: Long): SpotReport.Info = reportService.findSpotReportById(reportId)

    fun updateSpotSuggestionStatus(
        suggestionId: Long,
        status: SuggestionStatus,
    ): SpotSuggestion.Info {
        val suggestion = suggestionService.updateSuggestion(suggestionId, status)
        sendUpdateNotification(
            userId = suggestion.userId,
            type = "SUGGESTION",
            targetId = suggestion.id,
            body =
                when (status) {
                    SuggestionStatus.APPROVE -> NotificationMessages.APPROVE_SUGGESTION_CONTENTS
                    SuggestionStatus.REJECT -> NotificationMessages.REJECT_SUGGESTION_CONTENTS
                    else -> throw ErrorException(ErrorType.INVALID_UPDATE_SUGGESTION_STATUS)
                },
        )
        return suggestion
    }

    fun updateSpotReportStatus(updateReport: UpdateReport): SpotReport.Info {
        val report = reportService.updateSpotReport(updateReport)
        sendUpdateNotification(
            userId = report.userId,
            type = "REPORT",
            targetId = report.id,
            body =
                when (updateReport.status) {
                    ReportStatus.APPROVE -> NotificationMessages.APPROVE_REPORT_CONTENTS
                    ReportStatus.REJECT -> NotificationMessages.REJECT_REPORT_CONTENTS
                    else -> throw ErrorException(ErrorType.INVALID_UPDATE_REPORT_STATUS)
                },
        )
        return report
    }

    private fun sendUpdateNotification(
        userId: Long,
        type: String,
        targetId: Long,
        body: String,
    ) {
        val userDevice = userDeviceService.findByUserId(userId) ?: return

        val userProfile = userService.getProfile(userId)

        try {
            fcmSender.send(
                sendNotificationMessage =
                    SendNotificationMessage(
                        userId = userDevice.userId,
                        title = NotificationMessages.DEFAULT_TITLE,
                        body = userProfile.nickname + body,
                    ),
                type = type,
                parameterValue = targetId.toString(),
            )
        } catch (e: Exception) {
            log.warn("Failed to send notification for user $userId", e)
        }
    }
}
