package com.sseudam.report.component

import com.sseudam.report.ReportType
import com.sseudam.report.SpotReport
import com.sseudam.report.reject.ReportReject
import com.sseudam.report.repository.ReportRejectRepository
import com.sseudam.report.repository.SpotReportRepository
import com.sseudam.support.cursor.OffsetPageRequest
import com.sseudam.support.page.Page
import org.springframework.stereotype.Component

@Component
class ReportReader(
    private val reportRepository: SpotReportRepository,
    private val reportRejectRepository: ReportRejectRepository,
) {
    fun readAllByUserId(userId: Long): List<SpotReport.Info> = reportRepository.findAllInfoByUserId(userId)

    fun readBy(reportId: Long): SpotReport.Info = reportRepository.findById(reportId)

    fun readAllBy(
        offsetPageRequest: OffsetPageRequest,
        searchType: ReportType?,
    ): Page<SpotReport.Detail> = reportRepository.findAllBy(offsetPageRequest, searchType)

    fun existsByName(name: String): Boolean = reportRepository.existsByName(name)

    fun readRejectByReportId(reportId: Long): ReportReject.Info? = reportRejectRepository.findByReportId(reportId)

    fun readAllDetailByUserId(userId: Long): List<SpotReport.Detail> = reportRepository.findAllDetailsByUserId(userId)
}
