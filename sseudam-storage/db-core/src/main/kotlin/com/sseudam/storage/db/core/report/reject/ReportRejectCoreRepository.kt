package com.sseudam.storage.db.core.report.reject

import com.sseudam.report.reject.ReportRejectRepository
import org.springframework.stereotype.Repository

@Repository
class ReportRejectCoreRepository(
    private val reportRejectJpaRepository: ReportRejectJpaRepository,
) : ReportRejectRepository
