package com.sseudam.presentation.v1.pet.response

import com.sseudam.pet.Pet
import com.sseudam.pet.UserPet
import java.time.LocalDateTime

data class UserPetInfoResponse(
    val userId: Long,
    val petId: Long,
    val name: String,
    val point: Long,
    val levelType: Pet.LevelType,
    val maxLevelStandard: Long,
    val createdAt: LocalDateTime,
) {
    companion object {
        fun of(
            userPetInfo: UserPet.Info,
            levelType: Pet.LevelType,
            maxLevelStandard: Long,
        ): UserPetInfoResponse =
            UserPetInfoResponse(
                userId = userPetInfo.userId,
                petId = userPetInfo.petId,
                name = levelType.description,
                point = userPetInfo.point,
                levelType = levelType,
                maxLevelStandard = maxLevelStandard,
                createdAt = userPetInfo.createdAt,
            )
    }
}
