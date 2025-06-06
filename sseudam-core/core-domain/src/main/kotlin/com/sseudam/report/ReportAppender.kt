package com.sseudam.report

import org.locationtech.jts.geom.Coordinate
import org.locationtech.jts.geom.GeometryFactory
import org.springframework.stereotype.Component

@Component
class ReportAppender(
    private val spotReportRepository: SpotReportRepository,
) {
    companion object {
        private val GEOMETRY_FACTORY = GeometryFactory()
    }

    fun append(
        imageUrl: String,
        createSpotSuggestion: SpotReport.Create,
    ): SpotReport.Info {
        val point =
            GEOMETRY_FACTORY.createPoint(
                Coordinate(createSpotSuggestion.longitude, createSpotSuggestion.latitude),
            )
        return spotReportRepository.create(imageUrl, point, createSpotSuggestion)
    }
}
