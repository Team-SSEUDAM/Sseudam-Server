package com.sseudam.suggestion.dto

import com.sseudam.common.GeoJson
import com.sseudam.suggestion.SpotSuggestion
import com.sseudam.user.UserProfile
import java.time.LocalDateTime

data class SendMessageSuggestionDto(
    val id: Long,
    val site: String,
    val spotName: String,
    val trashType: String,
    val userId: Long,
    val nickname: String,
    val coordinateText: String,
    val createdAt: LocalDateTime,
) {
    companion object {
        fun of(
            suggestion: SpotSuggestion.Info,
            userProfile: UserProfile,
        ): SendMessageSuggestionDto {
            val pointCoordinate =
                when (val point = suggestion.point) {
                    is GeoJson.Point -> point.coordinates
                    else -> emptyList()
                }
            val coordinateText =
                if (pointCoordinate.size >= 2) {
                    "${pointCoordinate[0]}, ${pointCoordinate[1]} (경도, 위도)"
                } else {
                    "좌표 정보 없음"
                }

            return SendMessageSuggestionDto(
                id = suggestion.id,
                site = suggestion.address.site,
                spotName = suggestion.spotName,
                trashType = suggestion.trashType.displayName,
                userId = suggestion.userId,
                nickname = userProfile.nickname,
                coordinateText = coordinateText,
                createdAt = suggestion.createdAt,
            )
        }
    }
}
