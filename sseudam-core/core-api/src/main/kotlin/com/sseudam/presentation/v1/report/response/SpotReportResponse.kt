package com.sseudam.presentation.v1.report.response

import com.sseudam.common.Address
import com.sseudam.report.ReportStatus
import com.sseudam.report.ReportType
import com.sseudam.report.SpotReport
import com.sseudam.support.geo.GeoJson
import com.sseudam.trashspot.TrashType
import io.swagger.v3.oas.annotations.media.Schema
import java.time.LocalDateTime

@Schema(description = "신고 내역 응답")
data class SpotReportResponse(
    @Schema(description = "신고 내역 응답")
    val id: Long,
    @Schema(description = "장소 ID")
    val spotId: Long,
    @Schema(description = "신고자 ID")
    val userId: Long,
    @Schema(description = "신고 타입")
    val reportType: ReportType,
    @Schema(description = "신고 위치")
    val point: GeoJson,
    @Schema(description = "신고 주소")
    val address: Address,
    @Schema(description = "쓰레기통 타입")
    val trashType: TrashType,
    @Schema(description = "신고된 이미지 url")
    val imageUrl: String,
    @Schema(description = "신고 상태")
    val status: ReportStatus,
    @Schema(description = "신고 시간")
    val createdAt: LocalDateTime,
) {
    companion object {
        fun of(report: SpotReport.Info) =
            SpotReportResponse(
                id = report.id,
                spotId = report.spotId,
                userId = report.userId,
                reportType = report.reportType,
                point = report.point,
                address = report.address,
                trashType = report.trashType,
                imageUrl = report.imageUrl,
                status = report.status,
                createdAt = report.createdAt,
            )
    }
}
