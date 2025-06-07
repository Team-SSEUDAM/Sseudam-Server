package com.sseudam.trashspot.image

import org.springframework.stereotype.Component

@Component
class TrashSpotImageReader(
    private val trashSpotImageRepository: TrashSpotImageRepository,
) {
    fun readAllByTrashSpotIds(map: List<Long>): List<TrashSpotImage> = trashSpotImageRepository.findAllByTrashSpotIds(map)

    fun readBySpotId(spotId: Long): List<TrashSpotImage> = trashSpotImageRepository.findBySpotId(spotId)
}
