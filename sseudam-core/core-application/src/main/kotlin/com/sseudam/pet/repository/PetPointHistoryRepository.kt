package com.sseudam.pet.repository

import com.sseudam.pet.PetPointHistory

interface PetPointHistoryRepository {
    fun save(petPointHistory: PetPointHistory.Create): PetPointHistory.Info

    fun findAllByUserPet(userPetId: Long): List<PetPointHistory.Info>
}
