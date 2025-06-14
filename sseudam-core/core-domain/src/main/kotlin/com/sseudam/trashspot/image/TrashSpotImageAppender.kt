package com.sseudam.trashspot.image

import org.springframework.stereotype.Component

@Component
class TrashSpotImageAppender(
    private val trashSpotImageRepository: TrashSpotImageRepository,
) {
    fun append(createImage: TrashSpotImage.Create): TrashSpotImage.Info = trashSpotImageRepository.save(createImage)
}
