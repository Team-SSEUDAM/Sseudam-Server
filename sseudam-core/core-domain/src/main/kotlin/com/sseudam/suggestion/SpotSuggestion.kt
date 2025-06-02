package com.sseudam.suggestion

import com.sseudam.common.Address
import com.sseudam.support.geo.GeoJson
import com.sseudam.support.geo.Region
import com.sseudam.trashspot.TrashType

/**
 * SpotSuggestion
 * @property id 제보 id
 * @property userId 제보자 id
 * @property point 제보 위치
 * @property region 제보 지역
 * @property address 제보 주소
 * @property trashType 제보된 쓰레기통 타입
 * @property status 제보 상태
 * @property imageUrl 제보된 S3 imageUrl
 */
data class SpotSuggestion(
    val id: Long,
    val userId: Long,
    val point: GeoJson,
    val region: Region,
    val address: Address,
    val trashType: TrashType,
    val imageUrl: String,
    val status: SuggestionStatus = SuggestionStatus.WAITING,
)
