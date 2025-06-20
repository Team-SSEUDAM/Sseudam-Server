package com.sseudam.pet

interface PetPointHistoryRepository {
    fun findAllByUserPet(userPetId: Long): List<PetPointHistory.Info>
}
