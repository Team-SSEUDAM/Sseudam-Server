package com.sseudam.report.repository

import com.sseudam.report.ReportStatus
import com.sseudam.report.ReportType
import com.sseudam.report.SpotReport
import com.sseudam.support.page.OffsetPageRequest
import com.sseudam.support.page.Page
import org.locationtech.jts.geom.Point

interface SpotReportRepository {
    fun create(
        imageUrl: String,
        point: Point,
        createSpotReport: SpotReport.Create,
    ): SpotReport.Info

    fun findById(reportId: Long): SpotReport.Info

    fun findAllInfoByUserId(userId: Long): List<SpotReport.Info>

    fun findAllBy(
        offsetPageRequest: OffsetPageRequest,
        searchType: ReportType?,
        status: ReportStatus?,
    ): Page<SpotReport.Detail>

    fun findAllDetailsByUserId(userId: Long): List<SpotReport.Detail>

    fun update(
        reportId: Long,
        reportStatus: ReportStatus,
    ): SpotReport.Info

    fun cancel(reportId: Long)

    fun existsByName(name: String): Boolean

    fun deleteBy(reportId: Long)
}
