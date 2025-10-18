package com.sseudam.report.strategy

import com.sseudam.report.ReportType
import com.sseudam.report.SpotReport
import com.sseudam.trashspot.component.TrashSpotImageUpdater
import org.springframework.stereotype.Component

@Component
class UpdateReportPhotoProvider(
    private val trashSpotImageUpdater: TrashSpotImageUpdater,
) : ReportTypeStrategyProvider {
    override fun supports(reportType: ReportType): Boolean = reportType == ReportType.PHOTO

    override fun update(report: SpotReport.Info) {
        trashSpotImageUpdater.updateImage(report.spotId, report.imageUrl)
    }
}
