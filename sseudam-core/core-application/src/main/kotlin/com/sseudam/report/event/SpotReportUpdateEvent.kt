package com.sseudam.report.event

import com.sseudam.report.SpotReport

data class SpotReportUpdateEvent(
    val report: SpotReport.Info,
    val reason: String?,
    val rewardPoint: Long = 0L, // 승인 시 지급되는 포인트
)
