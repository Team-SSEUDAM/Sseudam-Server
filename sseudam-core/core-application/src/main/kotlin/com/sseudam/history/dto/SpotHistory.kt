package com.sseudam.history.dto

import com.sseudam.common.Address
import com.sseudam.common.GeoJson
import com.sseudam.trashspot.TrashType
import java.time.LocalDateTime

class SpotHistory {
    data class Info(
        val id: Long,
        val spotId: Long?,
        val userId: Long,
        val point: GeoJson,
        val spotName: String,
        val address: Address,
        val trashType: TrashType,
        val imageUrl: String,
        val status: HistoryStatus,
        val actionType: SpotActionType,
        val createdAt: LocalDateTime,
    )
}
