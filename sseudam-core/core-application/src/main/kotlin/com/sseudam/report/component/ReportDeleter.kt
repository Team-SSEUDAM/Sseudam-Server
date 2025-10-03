package com.sseudam.report.component

import com.sseudam.report.repository.SpotReportRepository
import org.springframework.stereotype.Component

@Component
class ReportDeleter(
    private val reportRepository: SpotReportRepository,
) {
    fun deleteBy(reportId: Long) {
        reportRepository.deleteBy(reportId)
    }
}
