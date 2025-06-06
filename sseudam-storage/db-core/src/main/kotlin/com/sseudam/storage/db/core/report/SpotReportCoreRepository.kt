package com.sseudam.storage.db.core.report

import com.sseudam.report.SpotReport
import com.sseudam.report.SpotReportRepository
import com.sseudam.support.tx.TxAdvice
import org.locationtech.jts.geom.Point
import org.springframework.stereotype.Repository

@Repository
class SpotReportCoreRepository(
    private val spotReportJpaRepository: SpotReportJpaRepository,
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
}
