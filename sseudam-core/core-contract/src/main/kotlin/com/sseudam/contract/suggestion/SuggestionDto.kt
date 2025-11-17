package com.sseudam.contract.suggestion

import com.sseudam.common.Address
import com.sseudam.common.GeoJson
import com.sseudam.common.Region
import com.sseudam.common.TrashType

/**
 * Suggestion 정보를 전달하기 위한 DTO
 *
 * 순환 의존성을 방지하기 위해 contract 모듈에 정의되어 있습니다.
 */
data class SuggestionDto(
    val id: Long,
    val userId: Long,
    val spotName: String,
    val point: GeoJson,
    val region: Region,
    val address: Address,
    val trashType: TrashType,
    val imageUrl: String,
    val status: String, // SuggestionStatus를 String으로 전달
)
