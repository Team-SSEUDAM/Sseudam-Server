package com.sseudam.admin.application.suggestion

import com.sseudam.common.Address
import com.sseudam.common.GeoJson
import com.sseudam.common.Region
import com.sseudam.suggestion.SpotSuggestion
import com.sseudam.suggestion.SuggestionStatus
import com.sseudam.common.TrashType
import com.sseudam.user.UserProfile
import java.time.LocalDateTime

data class AdminSpotSuggestionDetail(
    val id: Long,
    val userId: Long,
    val userName: String?,
    val spotName: String,
    val point: GeoJson,
    val region: Region,
    val address: Address,
    val trashType: TrashType,
    val imageUrl: String,
    val status: SuggestionStatus = SuggestionStatus.WAITING,
    val rejectReason: String?,
    val createdAt: LocalDateTime,
) {
    companion object {
        fun of(
            detail: SpotSuggestion.Detail,
            user: UserProfile?,
        ) = AdminSpotSuggestionDetail(
            id = detail.id,
            userId = detail.userId,
            userName = user?.name,
            spotName = detail.spotName,
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
