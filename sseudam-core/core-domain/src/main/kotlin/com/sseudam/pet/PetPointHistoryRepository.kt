package com.sseudam.pet

interface PetPointHistoryRepository {
    fun save(petPointHistory: PetPointHistory.Create): PetPointHistory.Info

    fun findAllByUserPet(userPetId: Long): List<PetPointHistory.Info>
}
