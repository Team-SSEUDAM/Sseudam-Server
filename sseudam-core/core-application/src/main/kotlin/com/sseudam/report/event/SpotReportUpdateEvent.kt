package com.sseudam.report.event

import com.sseudam.report.SpotReport

data class SpotReportUpdateEvent(
    val report: SpotReport.Info,
    val reason: String?,
)
