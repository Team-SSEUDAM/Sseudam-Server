package com.sseudam.admin.application

import com.sseudam.admin.application.report.AdminSpotReportDetail
import com.sseudam.admin.application.suggestion.AdminSpotSuggestionDetail
import com.sseudam.admin.domain.AdminToken
import com.sseudam.admin.domain.AdminUserProfile
import com.sseudam.auth.AuthenticationService
import com.sseudam.notification.NotificationService
import com.sseudam.notification.NotificationType
import com.sseudam.notification.command.CreateNotificationStoredCommand
import com.sseudam.notification.command.FirebaseCloudMessageCommand
import com.sseudam.notification.fcm.FcmSender
import com.sseudam.report.ReportFacade
import com.sseudam.report.ReportService
import com.sseudam.report.ReportStatus
import com.sseudam.report.ReportType
import com.sseudam.report.SpotReport
import com.sseudam.report.command.UpdateReportCommand
import com.sseudam.suggestion.SpotSuggestion
import com.sseudam.suggestion.SuggestionService
import com.sseudam.suggestion.SuggestionStatus
import com.sseudam.suggestion.command.UpdateSuggestionCommand
import com.sseudam.support.error.ErrorException
import com.sseudam.support.error.ErrorType
import com.sseudam.support.page.OffsetPageRequest
import com.sseudam.support.page.Page
import com.sseudam.trashspot.TrashSpotService
import com.sseudam.user.UserDeviceService
import com.sseudam.user.UserProfile
import com.sseudam.user.UserService
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
    private val notificationService: NotificationService,
    private val passwordEncoder: PasswordEncoder,
    private val reportFacade: ReportFacade,
) {
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

    fun reissue(refreshToken: String): AdminToken {
        val token = authService.adminReissue(refreshToken)
        return AdminToken(token.accessToken, token.refreshToken)
    }

    fun findByUser(userId: Long): AdminUserProfile {
        val profile = userService.getProfile(userId) ?: throw ErrorException(ErrorType.NOT_FOUND_USER)
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
    ): Page<AdminSpotSuggestionDetail> {
        val pages = suggestionService.findSuggestionsBy(offsetPageRequest, searchStatus)
        val users = userService.findAllBy(pages.content.map { it.userId }).associateBy { it.id }
        val contents =
            pages.content.map { suggestion ->
                AdminSpotSuggestionDetail.of(
                    suggestion,
                    users[suggestion.userId],
                )
            }
        return Page.of(
            content = contents,
            totalCount = pages.totalCount,
        )
    }

    fun findSuggestionDetails(suggestionId: Long): AdminSpotSuggestionDetail {
        val detail = suggestionService.findSpotSuggestionById(suggestionId)
        val user = userService.getProfile(detail.userId)
        return AdminSpotSuggestionDetail.of(detail, user)
    }

    fun findReports(
        offsetPageRequest: OffsetPageRequest,
        searchType: ReportType?,
        status: ReportStatus?,
    ): Page<AdminSpotReportDetail> {
        val pages = reportService.findReportsBy(offsetPageRequest, searchType, status)
        val users = userService.findAllBy(pages.content.map { it.userId }).associateBy { it.id }
        val contents =
            pages.content.map { report ->
                AdminSpotReportDetail.of(
                    report,
                    users[report.userId],
                )
            }
        return Page.of(
            content = contents,
            totalCount = pages.totalCount,
        )
    }

    fun findReportDetails(reportId: Long): AdminSpotReportDetail {
        val detail = reportFacade.findReportDetails(reportId)
        val user = userService.getProfile(detail.userId)
        return AdminSpotReportDetail.of(detail, user)
    }

    fun updateSpotSuggestionStatus(command: UpdateSuggestionCommand) =
        SpotSuggestion.UpdateResult.of(
            suggestionService.updateStatus(
                suggestionId = command.suggestionId,
                status = command.status,
                reason = command.reason,
                rewardPoint = command.rewardPoint,
            ),
        )

    fun updateSpotReportStatus(updateReportCommand: UpdateReportCommand): SpotReport.Info =
        reportService.updateSpotReport(updateReportCommand)

    fun pushToAllUsers(
        topic: String,
        contents: String,
    ) {
        val userDevice = userDeviceService.findAll()
        val userDevices =
            userDevice
                .sortedByDescending { it.createdAt }
                .filter { it.fcmToken.isNotBlank() && it.fcmToken.isNotEmpty() }
                .distinctBy { it.userId }
        if (userDevices.isEmpty()) return

        val deviceTokenMap = userDevices.associateBy { it.fcmToken }
        val messages =
            userDevices
                .map { device ->
                    FirebaseCloudMessageCommand(
                        fcmToken = device.fcmToken,
                        title = topic,
                        body = contents,
                        destination = "HomeView",
                    )
                }.toSet()
        fcmSender.sendAll(messages)
        notificationService.appendAll(
            messages
                .mapNotNull { message ->
                    CreateNotificationStoredCommand(
                        userId = deviceTokenMap[message.fcmToken]?.userId ?: return@mapNotNull null,
                        notificationStoredKey = "",
                        type = NotificationType.ADMIN_PUSH,
                        parameterValue = "",
                        topic = message.title,
                        contents = message.body,
                    )
                },
        )
    }
}
