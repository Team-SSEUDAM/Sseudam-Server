package com.sseudam.pet

import org.springframework.stereotype.Service
import java.time.Month

@Service
class PetLevelUpHistoryService(
    private val petLevelUpHistoryReader: PetLevelUpHistoryReader,
) {
    fun findAllBy(
        currentYear: Int,
        currentMonth: Month,
        userPetId: Long,
    ): List<PetLevelUpHistory.Info> =
        petLevelUpHistoryReader.readBy(
            currentYear = currentYear,
            currentMonth = currentMonth,
            userPetId = userPetId,
        )
}
