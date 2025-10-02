package com.sseudam.storage.db.core.pet

import com.sseudam.pet.PetLevelUpHistory
import com.sseudam.pet.PetLevelUpHistoryRepository
import com.sseudam.support.tx.Tx
import org.springframework.stereotype.Repository
import java.time.Month

@Repository
class PetLevelUpHistoryCoreHistoryRepository(
    private val petLevelUpHistoryJpaRepository: PetLevelUpHistoryJpaRepository,
) : PetLevelUpHistoryRepository {
    override fun save(petLevelUpHistory: PetLevelUpHistory.Create): PetLevelUpHistory.Info =
        Tx.writeable {
            petLevelUpHistoryJpaRepository
                .save(
                    PetLevelUpHistoryEntity(
                        petLevelUpHistory,
                    ),
                ).toPetLevelUpHistoryInfo()
        }

    override fun findAllBy(
        currentYear: Int,
        currentMonth: Month,
        userPetId: Long,
    ): List<PetLevelUpHistory.Info> =
        Tx.readable {
            petLevelUpHistoryJpaRepository
                .findAllByYearAndMonthlyAndUserPetId(currentYear, currentMonth, userPetId)
                .map { it.toPetLevelUpHistoryInfo() }
        }

    override fun findAllBy(userPetId: Long): List<PetLevelUpHistory.Info> =
        Tx.readable {
            petLevelUpHistoryJpaRepository
                .findAllByUserPetId(userPetId)
                .map { it.toPetLevelUpHistoryInfo() }
        }

    override fun findAllByUserId(userId: Long): List<PetLevelUpHistory.Info> =
        Tx.readable {
            petLevelUpHistoryJpaRepository
                .findAllByUserId(userId)
                .map { it.toPetLevelUpHistoryInfo() }
        }
}
