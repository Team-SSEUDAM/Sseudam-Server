package com.sseudam.storage.db.core.report.reject

import com.sseudam.report.reject.ReportReject
import com.sseudam.report.reject.ReportRejectRepository
import com.sseudam.support.tx.TxAdvice
import org.springframework.stereotype.Repository

@Repository
class ReportRejectCoreRepository(
    private val reportRejectJpaRepository: ReportRejectJpaRepository,
    private val txAdvice: TxAdvice,
) : ReportRejectRepository {
    override fun save(create: ReportReject.Create): ReportReject.Info =
        reportRejectJpaRepository.save(ReportRejectEntity(create)).toReportReject()

    override fun findByReportId(reportId: Long): ReportReject.Info? =
        txAdvice.readOnly {
            reportRejectJpaRepository.findByReportId(reportId)?.toReportReject()
        }
}
