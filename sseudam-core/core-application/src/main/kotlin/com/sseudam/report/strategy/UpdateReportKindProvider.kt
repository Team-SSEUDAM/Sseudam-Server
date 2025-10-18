package com.sseudam.report.strategy

import com.sseudam.report.ReportType
import com.sseudam.report.SpotReport
import com.sseudam.trashspot.component.TrashSpotUpdater
import org.springframework.stereotype.Component

@Component
class UpdateReportKindProvider(
    private val trashSpotUpdater: TrashSpotUpdater,
) : ReportTypeStrategyProvider {
    override fun supports(reportType: ReportType): Boolean = reportType == ReportType.KIND

    override fun update(report: SpotReport.Info) {
        trashSpotUpdater.updateType(report.spotId, report.trashType)
    }
}
