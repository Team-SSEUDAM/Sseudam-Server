package com.sseudam.pet

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
