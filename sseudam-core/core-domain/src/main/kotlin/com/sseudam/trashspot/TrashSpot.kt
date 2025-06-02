package com.sseudam.trashspot

import com.sseudam.common.Address
import com.sseudam.support.geo.GeoJson
import com.sseudam.support.geo.Region
import com.sseudam.trashspot.image.TrashSpotImage
import java.time.LocalDateTime

/**
 * TrashSpot
 * @property id 쓰레기통 id
 * @property description 쓰레기통 정보
 * @property region 쓰레기통 지역
 * @property address 쓰레기통 주소
 * @property point 쓰레기통 위치
 * @property trashType 쓰레기통 타입
 */
data class TrashSpot(
    val id: Long,
    val description: String,
    val region: Region,
    val address: Address,
    val point: GeoJson,
    val trashType: TrashType,
    val trashSpotImages: List<TrashSpotImage> = emptyList(),
    val updatedAt: LocalDateTime? = null,
)
