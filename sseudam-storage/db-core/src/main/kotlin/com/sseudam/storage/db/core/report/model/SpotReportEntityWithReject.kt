package com.sseudam.storage.db.core.report.model

import com.sseudam.storage.db.core.report.SpotReportEntity

data class SpotReportEntityWithReject(
    val entity: SpotReportEntity,
    val rejectReason: String?,
)
