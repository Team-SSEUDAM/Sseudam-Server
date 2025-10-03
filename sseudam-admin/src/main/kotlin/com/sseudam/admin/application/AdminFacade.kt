package com.sseudam.admin.application

import com.sseudam.admin.domain.AdminToken
import com.sseudam.admin.domain.AdminUserProfile
import com.sseudam.auth.AuthenticationService
import com.sseudam.notification.NotificationService
import com.sseudam.notification.command.CreateNotificationStoredCommand
import com.sseudam.notification.command.FirebaseCloudMessageCommand
import com.sseudam.notification.fcm.FcmSender
import com.sseudam.report.ReportFacade
import com.sseudam.report.ReportService
import com.sseudam.report.ReportType
import com.sseudam.report.SpotReport
import com.sseudam.report.command.UpdateReportCommand
import com.sseudam.suggestion.SpotSuggestion
import com.sseudam.suggestion.SuggestionService
import com.sseudam.suggestion.SuggestionStatus
import com.sseudam.suggestion.command.UpdateSuggestionCommand
import com.sseudam.support.cursor.OffsetPageRequest
import com.sseudam.support.error.ErrorException
import com.sseudam.support.error.ErrorType
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
    ): Page<SpotSuggestion.Detail> = suggestionService.findSuggestionsBy(offsetPageRequest, searchStatus)

    fun findSuggestionDetails(suggestionId: Long): SpotSuggestion.Detail = suggestionService.findSpotSuggestionById(suggestionId)

    fun findReports(
        offsetPageRequest: OffsetPageRequest,
        searchType: ReportType?,
    ): Page<SpotReport.Detail> = reportService.findReportsBy(offsetPageRequest, searchType)

    fun findReportDetails(reportId: Long): SpotReport.Detail = reportFacade.findReportDetails(reportId)

    fun updateSpotSuggestionStatus(command: UpdateSuggestionCommand) =
        SpotSuggestion.UpdateResult.of(
            suggestionService.updateStatus(command.suggestionId, command.status, command.reason),
        )

    fun updateSpotReportStatus(updateReportCommand: UpdateReportCommand): SpotReport.Info =
        reportService.updateSpotReport(updateReportCommand)

    fun pushToAllUsers(
        topic: String,
        contents: String,
    ) {
        val userDevices = userDeviceService.findAll()
        if (userDevices.isEmpty()) return

        val deviceTokenMap = userDevices.associateBy { it.fcmToken }
        val messages =
            userDevices
                .filter { it.fcmToken.isNotBlank() }
                .map { device ->
                    FirebaseCloudMessageCommand(
                        fcmToken = device.fcmToken,
                        title = topic,
                        body = contents,
                    )
                }.toSet()
        fcmSender.sendAll(messages)
        notificationService.appendAll(
            messages
                .mapNotNull { message ->
                    CreateNotificationStoredCommand(
                        userId = deviceTokenMap[message.fcmToken]?.userId ?: return@mapNotNull null,
                        notificationStoredKey = "",
                        type = "ADMIN_PUSH",
                        parameterValue = "/",
                        topic = message.title,
                        contents = message.body,
                    )
                },
        )
    }
}
