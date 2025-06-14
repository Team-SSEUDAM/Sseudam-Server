package com.sseudam.trashspot

import com.sseudam.common.Address
import com.sseudam.support.geo.GeoJson
import com.sseudam.support.geo.Region
import org.locationtech.jts.geom.Point
import java.time.LocalDateTime

class TrashSpot {
    /** TrashSpot Create
     * @property name 쓰레기통 정보
     * @property region 쓰레기통 지역
     * @property address 쓰레기통 주소
     * @property point 쓰레기통 위치
     * @property trashType 쓰레기통 타입
     */
    data class Create(
        val name: String,
        val region: Region,
        val address: Address,
        val point: Point,
        val trashType: TrashType,
    )

    /** TrashSpot Info
     * @property id 쓰레기통 id
     * @property name 쓰레기통 정보
     * @property region 쓰레기통 지역
     * @property address 쓰레기통 주소
     * @property point 쓰레기통 위치
     * @property trashType 쓰레기통 타입
     */
    data class Info(
        val id: Long,
        val name: String,
        val region: Region,
        val address: Address,
        val point: GeoJson,
        val trashType: TrashType,
        val updatedAt: LocalDateTime? = null,
    )
}
