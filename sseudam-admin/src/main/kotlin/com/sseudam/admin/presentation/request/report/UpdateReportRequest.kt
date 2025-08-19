package com.sseudam.admin.presentation.request.report

import com.sseudam.report.ReportStatus
import com.sseudam.report.UpdateReport
import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "신고 반영 요청 Json")
data class UpdateReportRequest(
    @Schema(description = "장소 id", example = "1")
    val spotId: Long,
    @Schema(description = "신고 상태", example = "APPROVE")
    val status: ReportStatus,
    @Schema(description = "반려 사유", example = "장소가 다름.")
    val reason: String?,
) {
    fun toCommand(reportId: Long) =
        UpdateReport(
            reportId = reportId,
            spotId = spotId,
            status = status,
            reason = reason,
        )
}
