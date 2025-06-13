package com.sseudam.storage.db.core.report

import com.sseudam.report.ReportType
import com.sseudam.report.SpotReport
import com.sseudam.report.SpotReportRepository
import com.sseudam.storage.db.core.support.findByIdOrElseThrow
import com.sseudam.support.cursor.OffsetPageRequest
import com.sseudam.support.tx.TxAdvice
import org.locationtech.jts.geom.Point
import org.springframework.stereotype.Repository

@Repository
class SpotReportCoreRepository(
    private val spotReportJpaRepository: SpotReportJpaRepository,
    private val spotReportCustomRepository: SpotReportCustomRepository,
    private val txAdvice: TxAdvice,
) : SpotReportRepository {
    override fun create(
        imageUrl: String,
        point: Point,
        createSpotReport: SpotReport.Create,
    ): SpotReport.Info =
        txAdvice.write {
            spotReportJpaRepository
                .save(
                    SpotReportEntity(imageUrl, point, createSpotReport),
                ).toSpotReport()
        }

    override fun findById(reportId: Long): SpotReport.Info =
        txAdvice.readOnly {
            spotReportJpaRepository
                .findByIdOrElseThrow(reportId)
                .toSpotReport()
        }

    override fun findAllByUserId(userId: Long): List<SpotReport.Info> =
        txAdvice.readOnly {
            spotReportJpaRepository
                .findAllByUserId(userId)
                .map { it.toSpotReport() }
        }

    override fun findAllBy(
        offsetPageRequest: OffsetPageRequest,
        searchType: ReportType?,
    ): List<SpotReport.Info> =
        txAdvice.readOnly {
            spotReportCustomRepository.findAllBy(offsetPageRequest, searchType)
        }
}
