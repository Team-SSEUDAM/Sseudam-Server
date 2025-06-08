package com.sseudam.report

import org.springframework.stereotype.Component

@Component
class ReportReader(
    private val reportRepository: SpotReportRepository,
) {
    fun readAllByUserId(userId: Long): List<SpotReport.Info> = reportRepository.findAllByUserId(userId)
}
