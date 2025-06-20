package com.sseudam.pet

import org.springframework.stereotype.Component
import java.time.Month

@Component
class PetLevelUpHistoryReader(
    private val petLevelUpHistoryRepository: PetLevelUpHistoryRepository,
) {
    fun readBy(
        currentYear: Int,
        currentMonth: Month,
        userPetId: Long,
    ): List<PetLevelUpHistory.Info> =
        petLevelUpHistoryRepository.findAllBy(
            currentYear = currentYear,
            currentMonth = currentMonth,
            userPetId = userPetId,
        )
}
