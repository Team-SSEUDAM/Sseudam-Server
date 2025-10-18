package com.sseudam.report.strategy

import com.sseudam.report.ReportType
import com.sseudam.report.SpotReport
import org.springframework.stereotype.Component

@Component
class UpdateReportEtcProvider : ReportTypeStrategyProvider {
    override fun supports(reportType: ReportType): Boolean = reportType == ReportType.ETC

    override fun update(report: SpotReport.Info) {
        // Do nothing
    }
}
