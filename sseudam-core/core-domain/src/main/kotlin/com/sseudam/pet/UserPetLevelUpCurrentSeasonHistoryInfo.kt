package com.sseudam.pet

import java.time.LocalDateTime
import java.time.Month

data class UserPetLevelUpCurrentSeasonHistoryInfo(
    val userId: Long,
    val nickname: String,
    val levelType: Pet.LevelType,
    val point: Long,
    val isLocked: Boolean,
    val year: Int,
    val month: Month,
    val createdAt: LocalDateTime,
)
