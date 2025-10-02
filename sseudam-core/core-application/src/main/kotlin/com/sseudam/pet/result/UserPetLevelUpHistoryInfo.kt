package com.sseudam.pet.result

import com.sseudam.pet.Pet
import java.time.LocalDateTime

data class UserPetLevelUpHistoryInfo(
    val userId: Long,
    val nickname: String,
    val levelType: Pet.LevelType,
    val point: Long,
    val season: String,
    val createdAt: LocalDateTime,
)
