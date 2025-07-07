package com.sseudam.report

import com.sseudam.support.cursor.OffsetPageRequest
import com.sseudam.support.page.Page
import org.locationtech.jts.geom.Point

interface SpotReportRepository {
    fun create(
        imageUrl: String,
        point: Point,
        createSpotReport: SpotReport.Create,
    ): SpotReport.Info

    fun findById(reportId: Long): SpotReport.Info

    fun findAllByUserId(userId: Long): List<SpotReport.Info>

    fun findAllBy(
        offsetPageRequest: OffsetPageRequest,
        searchType: ReportType?,
    ): Page<SpotReport.Info>

    fun update(
        reportId: Long,
        reportStatus: ReportStatus,
    ): SpotReport.Info

    fun existsByName(name: String): Boolean
}
