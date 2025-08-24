package com.sseudam.pet

import org.springframework.stereotype.Service

@Service
class PetPointHistoryService(
    private val petPointHistoryAppender: PetPointHistoryAppender,
    private val petPointHistoryReader: PetPointHistoryReader,
) {
    fun append(
        userPet: UserPet.Info,
        action: PetPointAction,
    ) {
        petPointHistoryAppender.append(
            userPet = userPet,
            action = action,
        )
    }

    fun findAllByUserPet(userPetId: Long): List<PetPointHistory.Info> = petPointHistoryReader.readAllByUserPet(userPetId)
}
