package com.sseudam.report

import com.sseudam.common.Address
import com.sseudam.support.geo.GeoJson
import com.sseudam.support.geo.Region
import com.sseudam.trashspot.TrashType
import java.time.LocalDateTime

class SpotReport {
    /** SpotReport Create
     * @property spotId 쓰레기통 위치 id
     * @property userId 신고자 id
     * @property reportType 신고 타입
     * @property latitude 제보 위치 위도
     * @property longitude 제보 위치 경도
     * @property spotName 쓰레기통 이름
     * @property region 제보 지역
     * @property city 제보 지역
     * @property site 제보 주소
     * @property trashType 쓰레기통 타입
     */
    data class Create(
        val spotId: Long,
        val userId: Long,
        val reportType: ReportType,
        val latitude: Double,
        val longitude: Double,
        val spotName: String,
        val region: Region,
        val city: String,
        val site: String,
        val trashType: TrashType,
    )

    /** SpotReport Info
     * @property id 쓰레기통 신고 id
     * @property spotId 쓰레기통 위치 id
     * @property userId 신고자 id
     * @property reportType 신고 타입
     * @property point 신고 위치
     * @property address 신고 주소
     * @property trashType 쓰레기통 타입
     * @property imageUrl 신고된 S3 imageUrl
     * @property status 신고 상태
     * @property createdAt 신고 시간
     */
    data class Info(
        val id: Long,
        val spotId: Long,
        val userId: Long,
        val reportType: ReportType,
        val point: GeoJson,
        val address: Address,
        val trashType: TrashType,
        val imageUrl: String,
        val status: ReportStatus = ReportStatus.WAITING,
        val createdAt: LocalDateTime,
    )
}
