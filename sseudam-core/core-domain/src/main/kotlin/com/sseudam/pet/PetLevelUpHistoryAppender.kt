package com.sseudam.pet

import org.springframework.stereotype.Component

@Component
class PetLevelUpHistoryAppender(
    private val petLevelUpHistoryRepository: PetLevelUpHistoryRepository,
) {
    fun append(create: PetLevelUpHistory.Create): PetLevelUpHistory.Info = petLevelUpHistoryRepository.save(create)
}
