package com.sseudam.pet

import java.time.LocalDateTime
import java.time.Month

/** 펫 성장 히스토리 */
class PetLevelUpHistory {
    data class Create(
        val userId: Long,
        val userPetId: Long,
        val nickname: String,
        val year: Int,
        val monthly: Month,
        val levelType: Pet.LevelType,
    )

    data class Info(
        val id: Long,
        val userId: Long,
        val userPetId: Long,
        val nickname: String,
        val year: Int,
        val monthly: Month,
        val levelType: Pet.LevelType,
        val createdAt: LocalDateTime,
        val updatedAt: LocalDateTime?,
    )
}
