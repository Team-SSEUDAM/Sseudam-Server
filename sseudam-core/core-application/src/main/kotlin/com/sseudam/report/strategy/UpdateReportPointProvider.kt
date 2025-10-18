package com.sseudam.report.strategy

import com.sseudam.common.GeoConverter
import com.sseudam.common.GeoJson
import com.sseudam.report.ReportType
import com.sseudam.report.SpotReport
import com.sseudam.trashspot.component.TrashSpotUpdater
import org.springframework.stereotype.Component

@Component
class UpdateReportPointProvider(
    private val trashSpotUpdater: TrashSpotUpdater,
    private val geoConverter: GeoConverter,
) : ReportTypeStrategyProvider {
    override fun supports(reportType: ReportType): Boolean = reportType == ReportType.POINT

    override fun update(report: SpotReport.Info) {
        val jtsPoint = geoConverter.geoJsonPointToJtsPoint(report.point as GeoJson.Point)
        trashSpotUpdater.updateLocation(report.spotId, report.region, jtsPoint)
    }
}
