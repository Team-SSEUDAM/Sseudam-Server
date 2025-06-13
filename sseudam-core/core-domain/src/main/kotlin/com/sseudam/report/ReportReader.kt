package com.sseudam.report

import com.sseudam.support.cursor.OffsetPageRequest
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
    ): List<SpotReport.Info> = reportRepository.findAllBy(offsetPageRequest, searchType)
}
