package com.sseudam.trashspot.component

import com.sseudam.trashspot.image.TrashSpotImage
import com.sseudam.trashspot.repository.TrashSpotImageRepository
import org.springframework.stereotype.Component

@Component
class TrashSpotImageReader(
    private val trashSpotImageRepository: TrashSpotImageRepository,
) {
    fun readAllByTrashSpotIds(spotIds: List<Long>): List<TrashSpotImage.Info> = trashSpotImageRepository.findAllByTrashSpotIds(spotIds)

    fun readBySpotId(spotId: Long): List<TrashSpotImage.Info> = trashSpotImageRepository.findAllBySpotId(spotId)
}
