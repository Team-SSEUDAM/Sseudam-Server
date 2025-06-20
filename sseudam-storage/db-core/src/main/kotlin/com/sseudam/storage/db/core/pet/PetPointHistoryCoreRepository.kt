package com.sseudam.storage.db.core.pet

import com.sseudam.pet.PetPointHistory
import com.sseudam.pet.PetPointHistoryRepository
import org.springframework.stereotype.Repository

@Repository
class PetPointHistoryCoreRepository(
    private val petPointHistoryJpaRepository: PetPointHistoryJpaRepository,
) : PetPointHistoryRepository {
    override fun findAllByUserPet(userPetId: Long): List<PetPointHistory.Info> =
        petPointHistoryJpaRepository
            .findAllByUserPetId(userPetId)
            .map { it.toPetPointHistoryInfo() }
}
