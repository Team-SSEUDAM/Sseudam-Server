package com.sseudam.report

import org.springframework.stereotype.Component

@Component
class ReportDeleter(
    private val reportRepository: SpotReportRepository,
) {
    fun deleteBy(reportId: Long) {
        reportRepository.deleteBy(reportId)
    }
}
