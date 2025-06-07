package com.sseudam.trashspot.image

import com.sseudam.trashspot.TrashSpot
import org.springframework.stereotype.Service

@Service
class TrashSpotImageService(
    private val trashSpotImageReader: TrashSpotImageReader,
) {
    fun findAll(trashSpots: List<TrashSpot>): List<TrashSpotImage> = trashSpotImageReader.readAllByTrashSpotIds(trashSpots.map { it.id })

    fun findBySpotId(spotId: Long): List<TrashSpotImage> = trashSpotImageReader.readBySpotId(spotId)
}
