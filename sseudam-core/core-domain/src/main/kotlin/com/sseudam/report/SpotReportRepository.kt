package com.sseudam.report

import org.locationtech.jts.geom.Point

interface SpotReportRepository {
    fun create(
        imageUrl: String,
        point: Point,
        createSpotReport: SpotReport.Create,
    ): SpotReport.Info
}
