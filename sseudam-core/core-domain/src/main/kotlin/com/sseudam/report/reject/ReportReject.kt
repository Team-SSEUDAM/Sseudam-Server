package com.sseudam.report.reject

import java.time.LocalDateTime

class ReportReject {
    data class Create(
        val reportId: Long,
        val reason: String,
    )

    data class Info(
        val reportId: Long,
        val reason: String?,
        val createdAt: LocalDateTime,
    )
}
