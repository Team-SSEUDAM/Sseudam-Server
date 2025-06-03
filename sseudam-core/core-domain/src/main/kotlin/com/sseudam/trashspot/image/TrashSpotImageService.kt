package com.sseudam.trashspot.image

import com.sseudam.trashspot.TrashSpot
import org.springframework.stereotype.Service

@Service
class TrashSpotImageService(
    private val trashSpotImageReader: TrashSpotImageReader,
) {
    fun findAll(trashSpots: List<TrashSpot>): List<TrashSpotImage> = trashSpotImageReader.findAllByTrashSpotIds(trashSpots.map { it.id })
}
