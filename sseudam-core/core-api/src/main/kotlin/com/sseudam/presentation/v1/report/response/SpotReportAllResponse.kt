package com.sseudam.presentation.v1.report.response

import com.sseudam.report.SpotReport
import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "사용자 신고 내역 응답 Json")
data class SpotReportAllResponse(
    val list: List<SpotReportResponse>,
) {
    companion object {
        fun of(list: List<SpotReport.Info>): SpotReportAllResponse = SpotReportAllResponse(list.map { SpotReportResponse.of(it) })
    }
}
