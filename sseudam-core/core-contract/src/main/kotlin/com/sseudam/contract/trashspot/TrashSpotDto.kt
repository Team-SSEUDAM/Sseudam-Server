package com.sseudam.contract.trashspot

import com.sseudam.common.Address
import com.sseudam.common.GeoJson
import com.sseudam.common.Region
import com.sseudam.common.TrashType
import java.time.LocalDateTime

/**
 * TrashSpot 정보를 전달하기 위한 DTO
 *
 * 순환 의존성을 방지하기 위해 contract 모듈에 정의되어 있습니다.
 */
data class TrashSpotDto(
    val id: Long,
    val name: String,
    val region: Region,
    val address: Address,
    val point: GeoJson,
    val trashType: TrashType,
    val suggesterId: Long? = null,
    val updatedAt: LocalDateTime? = null,
)
