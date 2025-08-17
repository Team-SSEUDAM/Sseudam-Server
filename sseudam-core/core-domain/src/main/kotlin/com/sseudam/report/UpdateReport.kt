package com.sseudam.report

data class UpdateReport(
    val reportId: Long,
    val spotId: Long,
    val status: ReportStatus,
)
