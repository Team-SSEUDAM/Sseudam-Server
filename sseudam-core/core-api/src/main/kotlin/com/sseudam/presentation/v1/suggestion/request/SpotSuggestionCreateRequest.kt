package com.sseudam.presentation.v1.suggestion.request

import com.sseudam.support.geo.Region
import com.sseudam.trashspot.TrashType
import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "제보하기 요청 Json")
data class SpotSuggestionCreateRequest(
    @Schema(description = "위도", example = "37.566535")
    val latitude: Double,
    @Schema(description = "경도", example = "126.977969")
    val longitude: Double,
    @Schema(description = "지역", example = "SEOUL")
    val region: Region,
    @Schema(description = "구/군/시", example = "강남구/거창군/동두천시")
    val city: String,
    @Schema(description = "주소", example = "서울시 강남구 강남동 1-4")
    val site: String,
    @Schema(description = "쓰레기통 유형", example = "GENERAL")
    val trashType: TrashType,
)
