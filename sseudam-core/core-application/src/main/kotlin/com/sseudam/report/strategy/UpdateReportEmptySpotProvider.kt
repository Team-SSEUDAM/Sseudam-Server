package com.sseudam.report.strategy

import com.sseudam.report.ReportType
import com.sseudam.report.SpotReport
import com.sseudam.trashspot.component.TrashSpotUpdater
import org.springframework.stereotype.Component

@Component
class UpdateReportEmptySpotProvider(
    private val trashSpotUpdater: TrashSpotUpdater,
) : ReportTypeStrategyProvider {
    override fun supports(reportType: ReportType): Boolean = reportType == com.sseudam.report.ReportType.EMPTY_SPOT

    override fun update(report: SpotReport.Info) {
        trashSpotUpdater.updateAsEmptySpot(report.spotId)
    }
}
