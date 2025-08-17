package com.sseudam.presentation.v1.report.response

import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "신고 시 쓰레기통 검증 응답 Json")
data class ReportValidationResponse(
    @Schema(description = "검증 결과", example = "true")
    val isValid: Boolean,
) {
    companion object {
        fun of(isValid: Boolean): ReportValidationResponse = ReportValidationResponse(isValid)
    }
}
