package com.sseudam.admin.presentation.response.suggestion

import com.sseudam.common.Address
import com.sseudam.common.GeoJson
import com.sseudam.common.Region
import com.sseudam.suggestion.SpotSuggestion
import com.sseudam.suggestion.SuggestionStatus
import com.sseudam.trashspot.TrashType
import io.swagger.v3.oas.annotations.media.Schema
import java.time.LocalDateTime

@Schema(description = "제보 내역 응답")
data class SpotSuggestionAdminResponse(
    @Schema(description = "제보 ID")
    val id: Long,
    @Schema(description = "제보 위치")
    val point: GeoJson,
    @Schema(description = "제보 장소 이름")
    val spotName: String,
    @Schema(description = "제보 지역")
    val region: Region,
    @Schema(description = "제보 주소")
    val address: Address,
    @Schema(description = "제보 쓰레기통 타입")
    val trashType: TrashType,
    @Schema(description = "제보된 쓰레기통 이미지")
    val imageUrl: String,
    @Schema(description = "제보 상태")
    val status: SuggestionStatus,
    @Schema(description = "거절 사유")
    val rejectReason: String? = null,
    @Schema(description = "제보 시간")
    val createdAt: LocalDateTime,
) {
    companion object {
        fun of(suggestion: SpotSuggestion.Detail) =
            SpotSuggestionAdminResponse(
                id = suggestion.id,
                point = suggestion.point,
                spotName = suggestion.spotName,
                region = suggestion.region,
                address = suggestion.address,
                trashType = suggestion.trashType,
                imageUrl = suggestion.imageUrl,
                status = suggestion.status,
                rejectReason = suggestion.rejectReason,
                createdAt = suggestion.createdAt,
            )
    }
}
