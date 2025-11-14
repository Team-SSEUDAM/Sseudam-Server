package com.sseudam.presentation.v1.suggestion.response

import com.sseudam.common.Address
import com.sseudam.common.GeoJson
import com.sseudam.common.Region
import com.sseudam.suggestion.SpotSuggestion
import com.sseudam.suggestion.SuggestionStatus
import com.sseudam.common.TrashType
import io.swagger.v3.oas.annotations.media.Schema
import java.time.LocalDateTime

@Schema(description = "제보 상세 내역 응답")
data class SpotSuggestionDetailResponse(
    @Schema(description = "제보 ID")
    val id: Long,
    @Schema(description = "제보 위치")
    val point: GeoJson,
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
    @Schema(description = "반려 사유")
    val rejectReason: String?,
    @Schema(description = "제보 시간")
    val createdAt: LocalDateTime,
) {
    companion object {
        fun from(detail: SpotSuggestion.Detail) =
            SpotSuggestionDetailResponse(
                id = detail.id,
                point = detail.point,
                region = detail.region,
                address = detail.address,
                trashType = detail.trashType,
                imageUrl = detail.imageUrl,
                status = detail.status,
                rejectReason = detail.rejectReason,
                createdAt = detail.createdAt,
            )
    }
}
