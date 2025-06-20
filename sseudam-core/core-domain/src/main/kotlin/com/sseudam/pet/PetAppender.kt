package com.sseudam.pet

import org.springframework.stereotype.Component

@Component
class PetAppender(
    private val petRepository: PetRepository,
) {
    fun appendSeasonPet() {
        // TODO: LastDay Scheduler Append
    }
}
