package com.sseudam.visit.event

import com.sseudam.pet.PetPointAction

data class SpotVisitedEvent(
    val spotId: Long,
    val suggesterId: Long?,
    val userId: Long,
    val petPointAction: PetPointAction,
)
