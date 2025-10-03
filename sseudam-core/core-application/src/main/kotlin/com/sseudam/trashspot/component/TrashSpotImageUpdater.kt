package com.sseudam.trashspot.component

import com.sseudam.trashspot.repository.TrashSpotImageRepository
import org.springframework.stereotype.Component

@Component
class TrashSpotImageUpdater(
    private val trashSpotImageRepository: TrashSpotImageRepository,
) {
    fun updateImage(
        spotId: Long,
        imageUrl: String,
    ) = trashSpotImageRepository.updateImage(spotId, imageUrl)
}
