package com.sseudam.admin.presentation.request.report

import com.sseudam.report.ReportStatus
import com.sseudam.report.UpdateReport

data class UpdateReportRequest(
    val reportId: Long,
    val spotId: Long,
    val status: ReportStatus,
) {
    fun toUpdateReport() =
        UpdateReport(
            reportId = reportId,
            spotId = spotId,
            status = status,
        )
}
