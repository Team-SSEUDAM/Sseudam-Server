package com.sseudam.report

import com.sseudam.support.cursor.OffsetPageRequest
import com.sseudam.support.page.Page
import org.springframework.stereotype.Component

@Component
class ReportReader(
    private val reportRepository: SpotReportRepository,
) {
    fun readAllByUserId(userId: Long): List<SpotReport.Info> = reportRepository.findAllByUserId(userId)

    fun readById(reportId: Long): SpotReport.Info = reportRepository.findById(reportId)

    fun readAllBy(
        offsetPageRequest: OffsetPageRequest,
        searchType: ReportType?,
    ): Page<SpotReport.Info> = reportRepository.findAllBy(offsetPageRequest, searchType)

    fun existsByName(name: String): Boolean = reportRepository.existsByName(name)
}
