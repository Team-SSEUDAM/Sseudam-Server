package com.sseudam.pet

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
                action = action,
                previousPoint = userPet.point,
                additionalPoint = action.point,
            ),
        )
}
