package com.sseudam.storage.db.core.report.reject

import org.springframework.data.jpa.repository.JpaRepository

interface ReportRejectJpaRepository : JpaRepository<ReportRejectEntity, Long> {
    fun findByReportId(reportId: Long): ReportRejectEntity?
}
