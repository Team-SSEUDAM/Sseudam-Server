package com.sseudam.pet.component

import com.sseudam.pet.PetLevelUpHistory
import com.sseudam.pet.repository.PetLevelUpHistoryRepository
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

    fun readBy(userPetId: Long): List<PetLevelUpHistory.Info> =
        petLevelUpHistoryRepository.findAllBy(
            userPetId = userPetId,
        )

    fun readByUser(userId: Long): List<PetLevelUpHistory.Info> = petLevelUpHistoryRepository.findAllByUserId(userId)
}
