package com.sseudam.report.repository

import com.sseudam.report.reject.ReportReject

interface ReportRejectRepository {
    fun save(create: ReportReject.Create): ReportReject.Info

    fun findByReportId(reportId: Long): ReportReject.Info?
}
