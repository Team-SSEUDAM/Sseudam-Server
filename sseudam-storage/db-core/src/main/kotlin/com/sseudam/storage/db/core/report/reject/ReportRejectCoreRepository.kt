package com.sseudam.storage.db.core.report.reject

import com.sseudam.report.reject.ReportReject
import com.sseudam.report.repository.ReportRejectRepository
import com.sseudam.support.tx.Tx
import org.springframework.stereotype.Repository

@Repository
class ReportRejectCoreRepository(
    private val reportRejectJpaRepository: ReportRejectJpaRepository,
) : ReportRejectRepository {
    override fun save(create: ReportReject.Create): ReportReject.Info =
        reportRejectJpaRepository.save(ReportRejectEntity(create)).toReportReject()

    override fun findByReportId(reportId: Long): ReportReject.Info? =
        Tx.readable {
            reportRejectJpaRepository.findByReportId(reportId)?.toReportReject()
        }
}
