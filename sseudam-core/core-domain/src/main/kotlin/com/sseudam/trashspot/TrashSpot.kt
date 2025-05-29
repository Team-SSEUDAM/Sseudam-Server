package com.sseudam.trashspot

import com.sseudam.support.geo.Region
import org.locationtech.jts.geom.Point
import java.time.LocalDateTime

/**
 * TrashSpot
 * @property id 쓰레기통 id
 * @property description 쓰레기통 정보
 * @property region 쓰레기통 지역
 * @property address 쓰레기통 주소
 * @property point 쓰레기통 위치
 * @property detail 쓰레기통 상세 정보
 * @property trashType 쓰레기통 타입
 */
data class TrashSpot(
    val id: Long,
    val description: String,
    val region: Region,
    val address: String,
    val point: Point,
    val detail: String?,
    val trashType: TrashType,
    val updatedAt: LocalDateTime? = null,
)
