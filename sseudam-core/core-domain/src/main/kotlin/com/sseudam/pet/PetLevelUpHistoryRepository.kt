package com.sseudam.pet

import java.time.Month

interface PetLevelUpHistoryRepository {
    fun findAllBy(
        currentYear: Int,
        currentMonth: Month,
        userPetId: Long,
    ): List<PetLevelUpHistory.Info>
}
