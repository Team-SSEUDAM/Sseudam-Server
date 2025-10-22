package com.sseudam.report.strategy

import com.sseudam.report.ReportType
import com.sseudam.report.SpotReport

interface ReportTypeStrategyProvider {
    fun supports(reportType: ReportType): Boolean

    fun update(report: SpotReport.Info)
}
