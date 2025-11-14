package com.sseudam.report.command

import com.sseudam.report.ReportStatus

data class UpdateReportCommand(
    val reportId: Long,
    val spotId: Long,
    val status: ReportStatus,
    val rejectReason: String?,
    val rewardPoint: Long = 0L, // 승인 시 지급되는 포인트
)
