package com.sseudam.report

import com.sseudam.report.reject.ReportReject
import com.sseudam.report.reject.ReportRejectRepository
import com.sseudam.support.cursor.OffsetPageRequest
import com.sseudam.support.page.Page
import org.springframework.stereotype.Component

@Component
class ReportReader(
    private val reportRepository: SpotReportRepository,
    private val reportRejectRepository: ReportRejectRepository,
) {
    fun readAllByUserId(userId: Long): List<SpotReport.Info> = reportRepository.findAllByUserId(userId)

    fun readById(reportId: Long): SpotReport.Info = reportRepository.findById(reportId)

    fun readAllBy(
        offsetPageRequest: OffsetPageRequest,
        searchType: ReportType?,
    ): Page<SpotReport.Info> = reportRepository.findAllBy(offsetPageRequest, searchType)

    fun existsByName(name: String): Boolean = reportRepository.existsByName(name)

    fun readRejectByReportId(reportId: Long): ReportReject.Info? = reportRejectRepository.findByReportId(reportId)
}
