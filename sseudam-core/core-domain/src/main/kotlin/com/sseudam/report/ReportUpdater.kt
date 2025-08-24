package com.sseudam.report

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
