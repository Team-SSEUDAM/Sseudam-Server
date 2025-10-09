package com.sseudam.storage.db.core.report

import com.sseudam.report.ReportStatus
import com.sseudam.report.ReportType
import com.sseudam.report.SpotReport
import com.sseudam.report.repository.SpotReportRepository
import com.sseudam.storage.db.core.support.findByIdAndDeletedAtIsNullOrElseThrow
import com.sseudam.storage.db.core.support.findByIdOrElseThrow
import com.sseudam.support.cursor.OffsetPageRequest
import com.sseudam.support.page.Page
import com.sseudam.support.tx.Tx
import org.locationtech.jts.geom.Point
import org.springframework.stereotype.Repository

@Repository
class SpotReportCoreRepository(
    private val spotReportJpaRepository: SpotReportJpaRepository,
    private val spotReportCustomRepository: SpotReportCustomRepository,
) : SpotReportRepository {
    override fun create(
        imageUrl: String,
        point: Point,
        createSpotReport: SpotReport.Create,
    ): SpotReport.Info =
        Tx.writeable {
            spotReportJpaRepository
                .save(
                    SpotReportEntity(imageUrl, point, createSpotReport),
                ).toSpotReport()
        }

    override fun findById(reportId: Long): SpotReport.Info =
        Tx.readable {
            spotReportJpaRepository
                .findByIdAndDeletedAtIsNullOrElseThrow(reportId)
                .toSpotReport()
        }

    override fun findAllInfoByUserId(userId: Long): List<SpotReport.Info> =
        Tx.readable {
            spotReportJpaRepository
                .findAllByUserIdAndDeletedAtIsNull(userId)
                .map { it.toSpotReport() }
        }

    override fun findAllBy(
        offsetPageRequest: OffsetPageRequest,
        searchType: ReportType?,
    ): Page<SpotReport.Detail> =
        Tx.readable {
            spotReportCustomRepository.findAllBy(offsetPageRequest, searchType)
        }

    override fun findAllDetailsByUserId(userId: Long): List<SpotReport.Detail> =
        Tx.readable {
            spotReportCustomRepository.findAllDetailsByUserId(userId)
        }

    override fun update(
        reportId: Long,
        reportStatus: ReportStatus,
    ): SpotReport.Info =
        Tx.writeable {
            val report =
                spotReportJpaRepository
                    .findByIdOrElseThrow(reportId)
            report.updateStatus(reportStatus).toSpotReport()
        }

    override fun cancel(
        reportId: Long,
        reportStatus: ReportStatus,
    ) = Tx.writeable {
        val report =
            spotReportJpaRepository
                .findByIdAndDeletedAtIsNullOrElseThrow(reportId)
        report.cancel(reportStatus)
    }

    override fun existsByName(name: String): Boolean =
        Tx.readable {
            spotReportJpaRepository.existsBySpotName(name)
        }

    override fun deleteBy(reportId: Long) =
        Tx.writeable {
            val report = spotReportJpaRepository.findByIdAndDeletedAtIsNullOrElseThrow(reportId)
            report.softDelete()
        }
}
