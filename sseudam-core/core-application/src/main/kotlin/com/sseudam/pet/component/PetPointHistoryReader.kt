package com.sseudam.pet.component

import com.sseudam.pet.PetPointHistory
import com.sseudam.pet.repository.PetPointHistoryRepository
import org.springframework.stereotype.Component

@Component
class PetPointHistoryReader(
    private val petPointHistoryRepository: PetPointHistoryRepository,
) {
    fun readAllByUserPet(userPetId: Long): List<PetPointHistory.Info> = petPointHistoryRepository.findAllByUserPet(userPetId)
}
