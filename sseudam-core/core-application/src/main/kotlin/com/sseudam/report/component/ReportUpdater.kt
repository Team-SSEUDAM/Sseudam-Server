package com.sseudam.report.component

import com.sseudam.report.ReportStatus
import com.sseudam.report.repository.SpotReportRepository
import org.springframework.stereotype.Component

@Component
class ReportUpdater(
    private val reportRepository: SpotReportRepository,
) {
    fun update(
        reportId: Long,
        reportStatus: ReportStatus,
    ) = reportRepository.update(reportId, reportStatus)
}
