package com.sseudam.report.result

import com.sseudam.report.SpotReport

data class CreateSpotReportResult(
    val spotReport: SpotReport.Info,
    val presignedUrl: String?,
)
