package com.sseudam.trashspot.component

import com.sseudam.trashspot.image.TrashSpotImage
import com.sseudam.trashspot.repository.TrashSpotImageRepository
import org.springframework.stereotype.Component

@Component
class TrashSpotImageAppender(
    private val trashSpotImageRepository: TrashSpotImageRepository,
) {
    fun append(createImage: TrashSpotImage.Create): TrashSpotImage.Info = trashSpotImageRepository.save(createImage)
}
