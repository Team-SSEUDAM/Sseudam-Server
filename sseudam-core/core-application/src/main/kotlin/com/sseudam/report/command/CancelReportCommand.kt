package com.sseudam.report.command

data class CancelReportCommand(
    val userId: Long,
    val reportId: Long,
)
