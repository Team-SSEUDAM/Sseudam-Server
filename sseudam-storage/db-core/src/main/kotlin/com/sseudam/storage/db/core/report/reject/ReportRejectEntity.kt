package com.sseudam.storage.db.core.report.reject

import com.sseudam.report.reject.ReportReject
import com.sseudam.storage.db.core.support.BaseEntity
import jakarta.persistence.Entity
import jakarta.persistence.Table

@Entity
@Table(name = "t_reject_report")
class ReportRejectEntity(
    val reportId: Long,
    val reason: String,
) : BaseEntity() {
    constructor(
        create: ReportReject.Create,
    ) : this (
        reportId = create.reportId,
        reason = create.reason,
    )

    fun toReportReject(): ReportReject.Info =
        ReportReject.Info(
            reportId = reportId,
            reason = reason,
            createdAt = createdAt,
        )
}
