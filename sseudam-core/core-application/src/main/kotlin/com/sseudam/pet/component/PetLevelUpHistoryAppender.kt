package com.sseudam.pet.component

import com.sseudam.pet.PetLevelUpHistory
import com.sseudam.pet.repository.PetLevelUpHistoryRepository
import org.springframework.stereotype.Component

@Component
class PetLevelUpHistoryAppender(
    private val petLevelUpHistoryRepository: PetLevelUpHistoryRepository,
) {
    fun append(create: PetLevelUpHistory.Create): PetLevelUpHistory.Info = petLevelUpHistoryRepository.save(create)
}
