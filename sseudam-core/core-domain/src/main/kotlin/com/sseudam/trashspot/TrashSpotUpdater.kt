package com.sseudam.trashspot

import org.locationtech.jts.geom.Point
import org.springframework.stereotype.Component

@Component
class TrashSpotUpdater(
    private val trashSpotRepository: TrashSpotRepository,
) {
    fun updateName(
        spotId: Long,
        name: String,
    ) {
        trashSpotRepository.updateName(spotId, name)
    }

    fun updateType(
        spotId: Long,
        type: TrashType,
    ) {
        trashSpotRepository.updateType(spotId, type)
    }

    fun updateLocation(
        spotId: Long,
        point: Point,
    ) {
        trashSpotRepository.updateLocation(spotId, point)
    }
}
