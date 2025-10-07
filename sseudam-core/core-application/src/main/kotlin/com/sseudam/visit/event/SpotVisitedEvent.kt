package com.sseudam.visit.event

import com.sseudam.pet.PetPointAction
import com.sseudam.trashspot.TrashSpot

data class SpotVisitedEvent(
    val spot: TrashSpot.Info,
    val userId: Long,
    val petPointAction: PetPointAction,
)
