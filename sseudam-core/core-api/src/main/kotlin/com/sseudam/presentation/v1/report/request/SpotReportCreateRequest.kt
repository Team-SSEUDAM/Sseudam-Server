package com.sseudam.presentation.v1.report.request

import com.sseudam.report.ReportType
import com.sseudam.support.geo.Region
import com.sseudam.trashspot.TrashType
import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "신고하기 요청 Json")
data class SpotReportCreateRequest(
    @Schema(description = "장소 ID", example = "1")
    val spotId: Long,
    @Schema(description = "신고 타입", example = "POINT")
    val reportType: ReportType,
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
