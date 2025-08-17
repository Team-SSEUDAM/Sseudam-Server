package com.sseudam.report.event

import com.sseudam.report.SpotReport

data class ReportUpdateEvent(
    val report: SpotReport.Info,
)
