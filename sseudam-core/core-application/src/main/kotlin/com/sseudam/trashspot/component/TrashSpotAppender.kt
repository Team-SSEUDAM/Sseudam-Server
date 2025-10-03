package com.sseudam.trashspot.component

import com.sseudam.trashspot.TrashSpot
import com.sseudam.trashspot.repository.TrashSpotRepository
import org.springframework.stereotype.Component

@Component
class TrashSpotAppender(
    private val trashSpotRepository: TrashSpotRepository,
) {
    fun append(createTrashSpot: TrashSpot.Create): TrashSpot.Info = trashSpotRepository.save(createTrashSpot)
}
