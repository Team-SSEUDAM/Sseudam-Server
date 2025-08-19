package com.sseudam.report.reject

interface ReportRejectRepository {
    fun save(create: ReportReject.Create): ReportReject.Info
}
