package com.sseudam.storage.db.core.pet

import com.sseudam.pet.PetPointHistory
import com.sseudam.pet.PetPointHistoryRepository
import com.sseudam.support.tx.Tx
import org.springframework.stereotype.Repository

@Repository
class PetPointHistoryCoreRepository(
    private val petPointHistoryJpaRepository: PetPointHistoryJpaRepository,
) : PetPointHistoryRepository {
    override fun save(petPointHistory: PetPointHistory.Create): PetPointHistory.Info =
        Tx.writeable {
            petPointHistoryJpaRepository
                .save(
                    PetPointHistoryEntity(
                        petPointHistory,
                    ),
                ).toPetPointHistoryInfo()
        }

    override fun findAllByUserPet(userPetId: Long): List<PetPointHistory.Info> =
        Tx.readable {
            petPointHistoryJpaRepository
                .findAllByUserPetId(userPetId)
                .map { it.toPetPointHistoryInfo() }
        }
}
