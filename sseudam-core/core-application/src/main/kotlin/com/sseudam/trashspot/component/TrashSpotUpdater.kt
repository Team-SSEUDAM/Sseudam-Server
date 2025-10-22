package com.sseudam.trashspot.component

import com.sseudam.common.Region
import com.sseudam.trashspot.TrashType
import com.sseudam.trashspot.repository.TrashSpotRepository
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
        region: Region,
        point: Point,
    ) {
        trashSpotRepository.updateLocation(spotId, region, point)
    }
}
