package com.sseudam.pet.result

import com.sseudam.pet.Pet
import java.time.LocalDateTime

data class UserPetLevelUpCurrentSeasonHistoryInfo(
    val userId: Long,
    val nickname: String,
    val levelType: Pet.LevelType,
    val point: Long,
    val isLocked: Boolean,
    val season: String,
    val createdAt: LocalDateTime,
)
