package com.sseudam.report.dto

import com.sseudam.common.GeoJson
import com.sseudam.report.ReportType
import com.sseudam.report.SpotReport
import java.time.LocalDateTime

data class SendMessageReportDto(
    val id: Long,
    val userId: Long,
    val nickname: String,
    val reportType: String,
    val reportBody: String,
    val createdAt: LocalDateTime,
) {
    companion object {
        fun of(
            report: SpotReport.Info,
            nickname: String,
        ) = SendMessageReportDto(
            id = report.id,
            userId = report.userId,
            nickname = nickname,
            reportType = report.reportType.displayName,
            reportBody = "**신고 내용:** ${
                when (report.reportType) {
                    ReportType.POINT -> {
                        when (val point = report.point) {
                            is GeoJson.Point -> {
                                val coords = point.coordinates
                                if (coords.size >= 2) {
                                    "${coords[0]}, ${coords[1]} (경도, 위도)"
                                } else {
                                    "좌표 정보 없음"
                                }
                            }
                            else -> "좌표 정보 없음"
                        }
                    }
                    ReportType.NAME -> report.spotName
                    ReportType.KIND -> report.trashType.displayName
                    ReportType.PHOTO -> report.imageUrl
                }
            }로 수정 요청",
            createdAt = report.createdAt,
        )
    }
}
