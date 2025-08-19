package com.sseudam.storage.db.core.report.reject

import com.sseudam.report.reject.ReportReject
import com.sseudam.report.reject.ReportRejectRepository
import org.springframework.stereotype.Repository

@Repository
class ReportRejectCoreRepository(
    private val reportRejectJpaRepository: ReportRejectJpaRepository,
) : ReportRejectRepository {
    override fun save(create: ReportReject.Create): ReportReject.Info =
        reportRejectJpaRepository.save(ReportRejectEntity(create)).toReportReject()
}
