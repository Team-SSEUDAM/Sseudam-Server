package com.sseudam.presentation.v1.pet.response

import com.sseudam.pet.Pet
import com.sseudam.pet.result.UserPetLevelUpCurrentSeasonHistoryInfo
import java.time.LocalDateTime

data class UserPetLevelHistoryCurrentResponse(
    val userId: Long,
    val nickname: String,
    val point: Long,
    val levelType: Pet.LevelType,
    val isLocked: Boolean,
    val season: String,
    val createdAt: LocalDateTime,
) {
    companion object {
        fun of(history: UserPetLevelUpCurrentSeasonHistoryInfo): UserPetLevelHistoryCurrentResponse =
            UserPetLevelHistoryCurrentResponse(
                userId = history.userId,
                nickname = history.nickname,
                point = history.point,
                levelType = history.levelType,
                isLocked = history.isLocked,
                season = history.season,
                createdAt = history.createdAt,
            )
    }
}
