package com.sseudam.admin.presentation.v1.report

import com.sseudam.admin.application.AdminFacade
import com.sseudam.admin.presentation.request.report.AdminUpdateReportRequest
import com.sseudam.admin.presentation.response.report.SpotReportAdminResponse
import com.sseudam.admin.presentation.response.report.SpotReportAllAdminResponse
import com.sseudam.admin.presentation.v1.annotation.AdminTagDocs
import com.sseudam.admin.presentation.v1.annotation.ApiAdminV1Controller
import com.sseudam.report.ReportType
import com.sseudam.support.cursor.OffsetPageRequest
import io.swagger.v3.oas.annotations.Operation
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestParam

@AdminTagDocs
@ApiAdminV1Controller
class AdminReportController(
    private val adminFacade: AdminFacade,
) {
    /** 어드민 신고 API */
    @Operation(summary = "신고 리스트 조회", description = "신고 리스트를 조회합니다.")
    @GetMapping("/reports")
    fun findReportsByPage(
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "20") size: Int,
        @RequestParam(required = false) searchType: ReportType?,
    ): SpotReportAllAdminResponse =
        SpotReportAllAdminResponse.of(
            adminFacade.findReports(OffsetPageRequest(page, size), searchType),
        )

    @Operation(summary = "신고 내역 상세 조회", description = "신고 내역을 상세 조회합니다.")
    @GetMapping("/reports/{reportId}")
    fun findReportDetails(
        @PathVariable reportId: Long,
    ): SpotReportAdminResponse = SpotReportAdminResponse.of(adminFacade.findReportDetails(reportId))

    @Operation(summary = "신고 반영", description = "신고 상태 변경과 생성을 합니다.")
    @PutMapping("/reports/{reportId}")
    fun updateReportStatus(
        @PathVariable reportId: Long,
        @RequestBody request: AdminUpdateReportRequest,
    ) = adminFacade.updateSpotReportStatus(request.toCommand(reportId))
}
