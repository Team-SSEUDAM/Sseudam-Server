package com.sseudam.pet

import org.springframework.stereotype.Component

@Component
class PetPointHistoryReader(
    private val petPointHistoryRepository: PetPointHistoryRepository,
) {
    fun readAllByUserPet(userPetId: Long): List<PetPointHistory.Info> = petPointHistoryRepository.findAllByUserPet(userPetId)
}
