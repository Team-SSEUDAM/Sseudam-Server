package com.sseudam.report.component

import com.sseudam.report.SpotReport
import com.sseudam.report.reject.ReportReject
import com.sseudam.report.repository.ReportRejectRepository
import com.sseudam.report.repository.SpotReportRepository
import org.locationtech.jts.geom.Coordinate
import org.locationtech.jts.geom.GeometryFactory
import org.locationtech.jts.geom.PrecisionModel
import org.springframework.stereotype.Component

@Component
class ReportAppender(
    private val spotReportRepository: SpotReportRepository,
    private val reportRejectRepository: ReportRejectRepository,
) {
    companion object {
        private val GEOMETRY_FACTORY = GeometryFactory(PrecisionModel(), 4326)
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

    fun appendReject(
        reportId: Long,
        reason: String?,
    ) {
        reportRejectRepository.save(ReportReject.Create(reportId, reason ?: ""))
    }
}
