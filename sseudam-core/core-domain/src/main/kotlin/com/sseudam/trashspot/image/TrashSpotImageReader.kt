package com.sseudam.trashspot.image

import org.springframework.stereotype.Component

@Component
class TrashSpotImageReader(
    private val trashSpotImageRepository: TrashSpotImageRepository,
) {
    fun readAllByTrashSpotIds(spotIds: List<Long>): List<TrashSpotImage.Info> = trashSpotImageRepository.findAllByTrashSpotIds(spotIds)

    fun readBySpotId(spotId: Long): List<TrashSpotImage.Info> = trashSpotImageRepository.findAllBySpotId(spotId)
}
