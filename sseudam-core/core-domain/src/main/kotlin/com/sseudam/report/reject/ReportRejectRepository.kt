package com.sseudam.report.reject

interface ReportRejectRepository {
    fun save(create: ReportReject.Create): ReportReject.Info

    fun findByReportId(reportId: Long): ReportReject.Info?

    fun findAllByReportIds(reportIds: List<Long>): List<ReportReject.Info>
}
