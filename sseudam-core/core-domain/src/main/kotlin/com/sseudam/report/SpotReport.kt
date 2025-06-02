package com.sseudam.report

import com.sseudam.common.Address
import com.sseudam.support.geo.GeoJson
import com.sseudam.trashspot.TrashType

/** SpotReport
 * @property id 쓰레기통 신고 id
 * @property spotId 쓰레기통 위치 id
 * @property userId 신고자 id
 * @property reportType 신고 타입
 * @property point 신고 위치
 * @property address 신고 주소
 * @property trashType 쓰레기통 타입
 * @property status 신고 상태
 */
data class SpotReport(
    val id: Long,
    val spotId: Long,
    val userId: Long,
    val reportType: ReportType,
    val point: GeoJson,
    val address: Address,
    val trashType: TrashType,
    val imageUrl: String,
    val status: ReportStatus = ReportStatus.WAITING,
)
