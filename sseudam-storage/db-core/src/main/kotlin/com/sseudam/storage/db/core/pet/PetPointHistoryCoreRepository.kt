package com.sseudam.storage.db.core.pet

import com.sseudam.pet.PetPointHistory
import com.sseudam.pet.PetPointHistoryRepository
import com.sseudam.support.tx.TxAdvice
import org.springframework.stereotype.Repository

@Repository
class PetPointHistoryCoreRepository(
    private val petPointHistoryJpaRepository: PetPointHistoryJpaRepository,
    private val txAdvice: TxAdvice,
) : PetPointHistoryRepository {
    override fun findAllByUserPet(userPetId: Long): List<PetPointHistory.Info> =
        txAdvice.readOnly {
            petPointHistoryJpaRepository
                .findAllByUserPetId(userPetId)
                .map { it.toPetPointHistoryInfo() }
        }
}
