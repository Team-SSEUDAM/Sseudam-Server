package com.sseudam.pet

import org.springframework.stereotype.Component

@Component
class UserPetUpdater(
    private val userPetRepository: UserPetRepository,
) {
    fun updatePetName(
        userId: Long,
        nickname: String,
    ): UserPet.Info = userPetRepository.updateNickname(userId, nickname)

    fun updatePetId(petId: Long): UserPet.Info = userPetRepository.updatePetId(petId)

    fun updatePoint(
        userPetId: Long,
        action: PetPointAction,
    ): UserPet.Info =
        userPetRepository.updatePoint(
            userPetId = userPetId,
            action = action,
        )
}
