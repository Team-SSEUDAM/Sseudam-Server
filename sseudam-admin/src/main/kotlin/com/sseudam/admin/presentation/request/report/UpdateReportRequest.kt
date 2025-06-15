package com.sseudam.admin.presentation.request.report

import com.sseudam.report.ReportStatus
import com.sseudam.report.UpdateReport

data class UpdateReportRequest(
    val spotId: Long,
    val status: ReportStatus,
) {
    fun toUpdateReport(reportId: Long) =
        UpdateReport(
            reportId = reportId,
            spotId = spotId,
            status = status,
        )
}
