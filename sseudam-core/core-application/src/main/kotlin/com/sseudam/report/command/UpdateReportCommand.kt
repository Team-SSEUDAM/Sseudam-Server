package com.sseudam.report.command

import com.sseudam.report.ReportStatus

data class UpdateReportCommand(
    val reportId: Long,
    val spotId: Long,
    val status: ReportStatus,
    val rejectReason: String?,
)
