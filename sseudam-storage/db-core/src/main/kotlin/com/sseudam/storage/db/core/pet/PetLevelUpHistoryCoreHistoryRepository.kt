package com.sseudam.storage.db.core.pet

import com.sseudam.pet.PetLevelUpHistory
import com.sseudam.pet.PetLevelUpHistoryRepository
import com.sseudam.support.tx.TxAdvice
import org.springframework.stereotype.Repository
import java.time.Month

@Repository
class PetLevelUpHistoryCoreHistoryRepository(
    private val petLevelUpHistoryJpaRepository: PetLevelUpHistoryJpaRepository,
    private val txAdvice: TxAdvice,
) : PetLevelUpHistoryRepository {
    override fun findAllBy(
        currentYear: Int,
        currentMonth: Month,
        userPetId: Long,
    ): List<PetLevelUpHistory.Info> =
        txAdvice.readOnly {
            petLevelUpHistoryJpaRepository
                .findAllByYearAndMonthlyAndUserPetId(currentYear, currentMonth, userPetId)
                .map { it.toPetLevelUpHistoryInfo() }
        }

    override fun findAllBy(userPetId: Long): List<PetLevelUpHistory.Info> =
        txAdvice.readOnly {
            petLevelUpHistoryJpaRepository
                .findAllByUserPetId(userPetId)
                .map { it.toPetLevelUpHistoryInfo() }
        }

    override fun findAllByUserId(userId: Long): List<PetLevelUpHistory.Info> =
        txAdvice.readOnly {
            petLevelUpHistoryJpaRepository
                .findAllByUserId(userId)
                .map { it.toPetLevelUpHistoryInfo() }
        }
}
