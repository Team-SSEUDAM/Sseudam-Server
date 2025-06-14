package com.sseudam.trashspot

import org.springframework.stereotype.Component

@Component
class TrashSpotAppender(
    private val trashSpotRepository: TrashSpotRepository,
) {
    fun append(createTrashSpot: TrashSpot.Create): TrashSpot.Info = trashSpotRepository.save(createTrashSpot)
}
