package com.sseudam.pet.component

import com.sseudam.pet.PetPointAction
import com.sseudam.pet.PetPointHistory
import com.sseudam.pet.UserPet
import com.sseudam.pet.repository.PetPointHistoryRepository
import org.springframework.stereotype.Component

@Component
class PetPointHistoryAppender(
    private val petPointHistoryRepository: PetPointHistoryRepository,
) {
    fun append(
        userPet: UserPet.Info,
        action: PetPointAction,
    ): PetPointHistory.Info =
        petPointHistoryRepository.save(
            PetPointHistory.Create(
                userPetId = userPet.id,
                pointAction = action,
                previousPoint = userPet.point,
                additionalPoint = action.point,
            ),
        )
}
