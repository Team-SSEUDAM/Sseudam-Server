package com.sseudam.admin.presentation.response.report

import com.sseudam.report.SpotReport
import com.sseudam.support.page.Page
import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "사용자 신고 내역 응답 Json")
data class SpotReportAllAdminResponse(
    val list: List<SpotReportAdminResponse>,
    val totalCount: Long,
) {
    companion object {
        fun of(page: Page<SpotReport.Detail>): SpotReportAllAdminResponse =
            SpotReportAllAdminResponse(
                list = page.content.map { SpotReportAdminResponse.of(it) },
                totalCount = page.totalCount,
            )
    }
}
