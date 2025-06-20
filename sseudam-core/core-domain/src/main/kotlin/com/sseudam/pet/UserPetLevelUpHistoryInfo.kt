package com.sseudam.pet

import java.time.LocalDateTime

data class UserPetLevelUpHistoryInfo(
    val userId: Long,
    val nickname: String,
    val levelType: Pet.LevelType,
    val point: Long,
    val createdAt: LocalDateTime,
)
