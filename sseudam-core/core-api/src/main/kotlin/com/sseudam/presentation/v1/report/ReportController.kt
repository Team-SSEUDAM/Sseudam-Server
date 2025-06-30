package com.sseudam.presentation.v1.report

import com.sseudam.presentation.v1.annotation.ApiV1Controller
import com.sseudam.presentation.v1.report.request.ReportValidationRequest
import com.sseudam.presentation.v1.report.request.SpotReportCreateRequest
import com.sseudam.presentation.v1.report.response.ReportImageUrlResponse
import com.sseudam.presentation.v1.report.response.ReportValidationResponse
import com.sseudam.presentation.v1.report.response.SpotReportAllResponse
import com.sseudam.report.ReportFacade
import com.sseudam.report.ReportService
import com.sseudam.report.SpotReport
import com.sseudam.user.User
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody

@Tag(name = "🚨 Report API", description = "신고 관련 API")
@ApiV1Controller
class ReportController(
    private val reportService: ReportService,
    private val reportFacade: ReportFacade,
) {
    @Operation(summary = "신고하기", description = "장소에 대한 신고를 합니다.")
    @PostMapping("/reports/{spotId}")
    fun reportSpotCreate(
        user: User,
        @PathVariable spotId: Long,
        @RequestBody request: SpotReportCreateRequest,
    ): ReportImageUrlResponse {
        val report =
            reportService.createSpotReport(
                report =
                    SpotReport.Create(
                        userId = user.id,
                        spotId = spotId,
                        reportType = request.reportType,
                        latitude = request.latitude,
                        longitude = request.longitude,
                        spotName = request.spotName,
                        region = request.region,
                        city = request.city,
                        site = request.site,
                        trashType = request.trashType,
                    ),
            )
        return ReportImageUrlResponse.of(
            report = report.first,
            s3ImageUrl = report.second,
        )
    }

    // TODO: cursor pagination (infinity scroll)
    @Operation(summary = "사용자 신고 내역 조회", description = "사용자가 신고한 내역을 조회합니다.")
    @GetMapping("/reports")
    fun reportSpotFindAll(user: User): SpotReportAllResponse {
        val reports = reportService.findAllReportByUserId(user.id)
        return SpotReportAllResponse.of(reports)
    }

    @Operation(summary = "신고 시 쓰레기통 검증", description = "신고 시 쓰레기통을 검증합니다.")
    @PostMapping("/reports/validate")
    fun reportSpotValidate(
        user: User,
        @RequestBody request: ReportValidationRequest,
    ): ReportValidationResponse {
        val isValid = reportFacade.validateSpotReport(request.name)
        return ReportValidationResponse.of(isValid)
    }
}
