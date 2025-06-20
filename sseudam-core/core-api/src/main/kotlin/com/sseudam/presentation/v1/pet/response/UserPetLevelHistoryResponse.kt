package com.sseudam.presentation.v1.pet.response

import com.sseudam.pet.Pet
import com.sseudam.pet.UserPetLevelUpHistoryInfo
import java.time.LocalDateTime

data class UserPetLevelHistoryResponse(
    val userId: Long,
    val nickname: String,
    val point: Long,
    val levelType: Pet.LevelType,
    val createdAt: LocalDateTime,
) {
    companion object {
        fun of(history: UserPetLevelUpHistoryInfo): UserPetLevelHistoryResponse =
            UserPetLevelHistoryResponse(
                userId = history.userId,
                nickname = history.nickname,
                point = history.point,
                levelType = history.levelType,
                createdAt = history.createdAt,
            )
    }
}
