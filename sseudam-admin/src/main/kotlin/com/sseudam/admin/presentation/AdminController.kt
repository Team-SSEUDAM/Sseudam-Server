package com.sseudam.admin.presentation

import com.sseudam.admin.application.AdminFacade
import com.sseudam.admin.presentation.request.AdminLoginRequest
import com.sseudam.admin.presentation.request.AdminRefreshTokenRequest
import com.sseudam.admin.presentation.request.AdminTokenRequest
import com.sseudam.admin.presentation.request.report.UpdateReportRequest
import com.sseudam.admin.presentation.response.AdminLogoutResponse
import com.sseudam.admin.presentation.response.AdminTokenResponse
import com.sseudam.admin.presentation.response.report.SpotReportAllResponse
import com.sseudam.admin.presentation.response.report.SpotReportResponse
import com.sseudam.admin.presentation.response.suggestion.SpotSuggestionAllResponse
import com.sseudam.admin.presentation.response.suggestion.SpotSuggestionResponse
import com.sseudam.admin.presentation.response.user.AdminUserResponse
import com.sseudam.admin.presentation.response.user.UserPageResponse
import com.sseudam.report.ReportType
import com.sseudam.suggestion.SuggestionStatus
import com.sseudam.support.cursor.OffsetPageRequest
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@Tag(name = "🔐 Admin API", description = "관리자 관련 API 입니다.")
@RestController
@RequestMapping("/api/v1/admin")
class AdminController(
    private val adminFacade: AdminFacade,
) {
    @Operation(summary = "어드민 로그인", description = "어드민 로그인을 합니다.")
    @PostMapping("/login")
    fun login(
        @RequestBody request: AdminLoginRequest,
    ): AdminTokenResponse {
        val token = adminFacade.login(request.loginId, request.password)
        return AdminTokenResponse.of(token)
    }

    @Operation(summary = "어드민 로그아웃", description = "어드민 로그아웃을 합니다.")
    @PostMapping("/logout")
    fun logout(
        @RequestBody request: AdminTokenRequest,
    ): AdminLogoutResponse {
        adminFacade.logout(request.accessToken)
        return AdminLogoutResponse(
            message = "로그아웃 되었습니다.",
        )
    }

    @Operation(summary = "어드민 토큰 재발급", description = "어드민 토큰을 재발급합니다.")
    @PostMapping("/reissue")
    fun reissueToken(
        @RequestBody request: AdminRefreshTokenRequest,
    ): AdminTokenResponse {
        val token = adminFacade.reissue(request.toRefreshToken())
        return AdminTokenResponse.of(token)
    }

    /** 어드민 사용자 API */
    @Operation(summary = "사용자 리스트 조회", description = "사용자 리스트를 조회합니다.")
    @GetMapping("/users")
    fun findUsersByPage(
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "20") size: Int,
    ): UserPageResponse =
        UserPageResponse.of(
            adminFacade.findUsers(OffsetPageRequest(page, size)),
        )

    @Operation(summary = "사용자 정보 조회", description = "사용자 정보를 조회합니다.")
    @GetMapping("/users/{userId}")
    fun findOneUser(
        @PathVariable("userId") userId: Long,
    ): AdminUserResponse = AdminUserResponse.of(adminFacade.findByUser(userId))

    /** 어드민 제보 API */
    @Operation(summary = "제보 리스트 조회", description = "제보 리스트를 조회합니다.")
    @GetMapping("/suggestions")
    fun findSuggestionsByPage(
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "20") size: Int,
        @RequestParam searchStatus: SuggestionStatus?,
    ): SpotSuggestionAllResponse =
        SpotSuggestionAllResponse.of(
            adminFacade.findSuggestions(OffsetPageRequest(page, size), searchStatus),
        )

    @Operation(summary = "제보 내역 상세 조회", description = "제보 내역을 상세 조회합니다.")
    @GetMapping("/suggestions/{suggestionId}")
    fun findSuggestionDetails(
        @PathVariable suggestionId: Long,
    ): SpotSuggestionResponse = SpotSuggestionResponse.of(adminFacade.findSuggestionDetails(suggestionId))

    @Operation(summary = "제보 반영", description = "제보 상태 변경과 생성을 합니다.")
    @PutMapping("/suggestions/{suggestionId}")
    fun updateSuggestionStatus(
        @PathVariable suggestionId: Long,
        @RequestParam status: SuggestionStatus,
    ) = adminFacade.updateSpotSuggestionStatus(suggestionId, status)

    /** 어드민 신고 API */
    @Operation(summary = "신고 리스트 조회", description = "신고 리스트를 조회합니다.")
    @GetMapping("/reports")
    fun findReportsByPage(
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "20") size: Int,
        @RequestParam searchType: ReportType?,
    ): SpotReportAllResponse =
        SpotReportAllResponse.of(
            adminFacade.findReports(OffsetPageRequest(page, size), searchType),
        )

    @Operation(summary = "신고 내역 상세 조회", description = "신고 내역을 상세 조회합니다.")
    @GetMapping("/reports/{reportId}")
    fun findReportDetails(
        @PathVariable reportId: Long,
    ): SpotReportResponse = SpotReportResponse.of(adminFacade.findReportDetails(reportId))

    @Operation(summary = "신고 반영", description = "신고 상태 변경과 생성을 합니다.")
    @PutMapping("/reports/{reportId}")
    fun updateReportStatus(
        @PathVariable reportId: Long,
        @RequestBody request: UpdateReportRequest,
    ) = adminFacade.updateSpotReportStatus(request.toUpdateReport(reportId))
}
