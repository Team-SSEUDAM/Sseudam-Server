package com.sseudam.trashspot

import com.sseudam.support.geo.Region
import com.sseudam.trashspot.image.TrashSpotImageService
import org.springframework.stereotype.Service

@Service
class TrashSpotFacade(
    private val trashSpotService: TrashSpotService,
    private val trashSpotImageService: TrashSpotImageService,
) {
    fun findAll(
        region: Region?,
        location: TrashSpotLocation,
    ): List<TrashSpot> {
        val trashSpots = trashSpotService.findAll(region, location)
        return trashSpots
    }
}
