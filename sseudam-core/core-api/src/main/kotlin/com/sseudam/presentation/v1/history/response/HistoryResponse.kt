package com.sseudam.presentation.v1.history.response

import com.sseudam.common.Address
import com.sseudam.common.GeoJson
import com.sseudam.history.dto.HistoryStatus
import com.sseudam.history.dto.SpotActionType
import com.sseudam.history.dto.SpotHistory
import com.sseudam.common.TrashType
import io.swagger.v3.oas.annotations.media.Schema
import java.time.LocalDateTime

data class HistoryResponse(
    @Schema(description = "신고 내역 응답")
    val id: Long,
    @Schema(description = "장소 ID")
    val spotId: Long?,
    @Schema(description = "신고자 ID")
    val userId: Long,
    @Schema(description = "신고 위치")
    val point: GeoJson,
    @Schema(description = "장소 이름")
    val spotName: String,
    @Schema(description = "신고 주소")
    val address: Address,
    @Schema(description = "쓰레기통 타입")
    val trashType: TrashType,
    @Schema(description = "신고된 이미지 url")
    val imageUrl: String,
    @Schema(description = "내역 상태")
    val status: HistoryStatus,
    @Schema(description = "제보/신고 여부")
    val actionType: SpotActionType,
    @Schema(description = "생성일자")
    val createdAt: LocalDateTime,
) {
    companion object {
        fun from(info: SpotHistory.Info): HistoryResponse =
            with(info) {
                HistoryResponse(
                    id = id,
                    spotId = spotId,
                    userId = userId,
                    point = point,
                    spotName = spotName,
                    address = address,
                    trashType = trashType,
                    imageUrl = imageUrl,
                    status = status,
                    actionType = actionType,
                    createdAt = createdAt,
                )
            }
    }
}
