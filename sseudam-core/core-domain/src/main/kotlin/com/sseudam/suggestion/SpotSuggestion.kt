package com.sseudam.suggestion

import com.sseudam.common.Address
import com.sseudam.support.geo.GeoJson
import com.sseudam.support.geo.Region
import com.sseudam.trashspot.TrashType
import java.time.LocalDateTime

class SpotSuggestion {
    /**
     * SpotSuggestion Create
     * @property userId 제보자 id
     * @property spotName 쓰레기통 이름
     * @property latitude 제보 위치 위도
     * @property longitude 제보 위치 경도
     * @property region 제보 지역
     * @property city 제보 지역
     * @property site 제보 주소
     * @property trashType 제보된 쓰레기통 타입
     */
    data class Create(
        val userId: Long,
        val spotName: String,
        val latitude: Double,
        val longitude: Double,
        val region: Region,
        val city: String,
        val site: String,
        val trashType: TrashType,
    )

    /**
     * SpotSuggestion Info
     * @property id 제보 id
     * @property userId 제보자 id
     * @property spotName 쓰레기통 이름
     * @property point 제보 위치
     * @property region 제보 지역
     * @property address 제보 주소
     * @property trashType 제보된 쓰레기통 타입
     * @property status 제보 상태
     * @property imageUrl 제보된 S3 imageUrl
     * @property createdAt 제보 시간
     */
    data class Info(
        val id: Long,
        val userId: Long,
        val spotName: String,
        val point: GeoJson,
        val region: Region,
        val address: Address,
        val trashType: TrashType,
        val imageUrl: String,
        val status: SuggestionStatus = SuggestionStatus.WAITING,
        val createdAt: LocalDateTime,
    )
}
