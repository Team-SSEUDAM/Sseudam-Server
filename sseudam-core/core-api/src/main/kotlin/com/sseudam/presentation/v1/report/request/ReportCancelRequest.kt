package com.sseudam.presentation.v1.report.request

import com.sseudam.report.command.CancelReportCommand
import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "신고 취소 요청")
data class ReportCancelRequest(
    @Schema(description = "신고 ID", example = "1")
    val reportId: Long,
) {
    fun toCommand(userId: Long) = CancelReportCommand(userId, reportId)
}
