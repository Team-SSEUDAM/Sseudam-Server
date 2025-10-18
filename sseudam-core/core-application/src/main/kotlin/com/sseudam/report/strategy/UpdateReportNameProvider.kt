package com.sseudam.report.strategy

import com.sseudam.report.ReportType
import com.sseudam.report.SpotReport
import com.sseudam.trashspot.component.TrashSpotUpdater
import org.springframework.stereotype.Component

@Component
class UpdateReportNameProvider(
    private val trashSpotUpdater: TrashSpotUpdater,
) : ReportTypeStrategyProvider {
    override fun supports(reportType: ReportType): Boolean = reportType == ReportType.NAME

    override fun update(report: SpotReport.Info) {
        trashSpotUpdater.updateName(report.spotId, report.spotName)
    }
}
