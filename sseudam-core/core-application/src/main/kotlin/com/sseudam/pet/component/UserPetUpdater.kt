package com.sseudam.pet.component

import com.sseudam.pet.PetPointAction
import com.sseudam.pet.UserPet
import com.sseudam.pet.repository.UserPetRepository
import org.springframework.stereotype.Component

@Component
class UserPetUpdater(
    private val userPetRepository: UserPetRepository,
) {
    fun updatePetName(
        userId: Long,
        nickname: String,
    ): UserPet.Info = userPetRepository.updateNickname(userId, nickname)

    fun updatePetId(
        userPetId: Long,
        petId: Long,
    ): UserPet.Info = userPetRepository.updatePetId(userPetId, petId)

    fun updatePointByAction(
        userPetId: Long,
        action: PetPointAction,
    ): UserPet.Info =
        userPetRepository.updatePointByAction(
            userPetId = userPetId,
            action = action,
        )

    fun initPoint(petId: Long) {
        userPetRepository.initPoint(petId)
    }
}
