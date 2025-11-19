package com.sseudam.admin.application.report

import com.sseudam.common.Address
import com.sseudam.common.GeoJson
import com.sseudam.common.Region
import com.sseudam.common.TrashType
import com.sseudam.report.ReportStatus
import com.sseudam.report.ReportType
import com.sseudam.report.SpotReport
import com.sseudam.user.UserProfile
import java.time.LocalDateTime

data class AdminSpotReportDetail(
    val id: Long,
    val spotId: Long,
    val userId: Long,
    val userName: String?,
    val reportType: ReportType,
    val point: GeoJson,
    val spotName: String,
    val region: Region = Region.UNKNOWN,
    val address: Address,
    val trashType: TrashType,
    val imageUrl: String,
    val status: ReportStatus = ReportStatus.WAITING,
    val rejectReason: String?,
    val reason: String?,
    val createdAt: LocalDateTime,
) {
    companion object {
        fun of(
            detail: SpotReport.Detail,
            user: UserProfile?,
        ): AdminSpotReportDetail =
            with(detail) {
                AdminSpotReportDetail(
                    id = id,
                    spotId = spotId,
                    userId = userId,
                    userName = user?.name,
                    reportType = reportType,
                    point = point,
                    spotName = spotName,
                    region = region,
                    address = address,
                    trashType = trashType,
                    imageUrl = imageUrl,
                    status = status,
                    rejectReason = rejectReason,
                    reason = reason,
                    createdAt = createdAt,
                )
            }
    }
}
