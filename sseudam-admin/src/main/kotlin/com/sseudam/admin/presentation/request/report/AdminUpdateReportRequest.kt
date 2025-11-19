package com.sseudam.admin.presentation.request.report

import com.sseudam.report.ReportStatus
import com.sseudam.report.command.UpdateReportCommand
import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "신고 반영 요청 Json")
data class AdminUpdateReportRequest(
    @Schema(description = "장소 id", example = "1")
    val spotId: Long,
    @Schema(description = "신고 상태", example = "APPROVE")
    val status: ReportStatus,
    @Schema(description = "반려 사유", example = "장소가 다름.")
    val rejectReason: String? = null,
) {
    companion object {
        private const val REPORT_APPROVED_POINT = 15L
    }

    fun toCommand(reportId: Long) =
        UpdateReportCommand(
            reportId = reportId,
            spotId = spotId,
            status = status,
            rejectReason = rejectReason,
            rewardPoint = if (status == ReportStatus.APPROVE) REPORT_APPROVED_POINT else 0L,
        )
}
