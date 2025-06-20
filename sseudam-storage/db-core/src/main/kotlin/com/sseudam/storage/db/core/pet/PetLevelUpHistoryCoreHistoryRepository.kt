package com.sseudam.storage.db.core.pet

import com.sseudam.pet.PetLevelUpHistory
import com.sseudam.pet.PetLevelUpHistoryRepository
import org.springframework.stereotype.Repository
import java.time.Month

@Repository
class PetLevelUpHistoryCoreHistoryRepository(
    private val petLevelUpHistoryJpaRepository: PetLevelUpHistoryJpaRepository,
) : PetLevelUpHistoryRepository {
    override fun findAllBy(
        currentYear: Int,
        currentMonth: Month,
        userPetId: Long,
    ): List<PetLevelUpHistory.Info> =
        petLevelUpHistoryJpaRepository
            .findAllByYearAndMonthlyAndUserPetId(currentYear, currentMonth, userPetId)
            .map { it.toPetLevelUpHistoryInfo() }
}
