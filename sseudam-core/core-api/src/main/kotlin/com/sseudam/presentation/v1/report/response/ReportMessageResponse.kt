package com.sseudam.presentation.v1.report.response

import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "신고 취소 응답")
data class ReportMessageResponse(
    @Schema(description = "응답 메시지", example = "신고가 취소되었습니다.")
    val message: String,
)
