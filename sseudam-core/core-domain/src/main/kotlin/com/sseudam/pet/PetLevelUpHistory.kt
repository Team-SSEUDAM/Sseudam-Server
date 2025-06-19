package com.sseudam.pet

import java.time.LocalDateTime

/** 펫 성장 히스토리 */
class PetLevelUpHistory {
    data class Create(
        val userPetId: Long,
        val levelType: Pet.LevelType,
    )

    data class Info(
        val id: Long,
        val userPetId: Long,
        val levelType: Pet.LevelType,
        val createdAt: LocalDateTime,
        val updatedAt: LocalDateTime?,
    )
}
