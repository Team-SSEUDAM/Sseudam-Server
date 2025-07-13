package com.sseudam.presentation.v1.report.response

import com.sseudam.report.SpotReport
import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "신고하기 PresignedUrl 응답 Json")
data class ReportImageUrlResponse(
    @Schema(description = "신고 ID")
    val reportId: Long,
    @Schema(description = "PresignedUrl")
    val presignedUrl: String,
) {
    companion object {
        fun of(
            report: SpotReport.Info,
            presignedUrl: String,
        ) = ReportImageUrlResponse(report.id, presignedUrl)
    }
}
