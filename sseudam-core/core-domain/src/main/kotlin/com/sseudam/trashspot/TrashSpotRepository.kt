package com.sseudam.trashspot

import com.sseudam.support.geo.Region

interface TrashSpotRepository {
    fun findAll(): List<TrashSpot>

    fun findAllByRegion(region: Region): List<TrashSpot>

    fun findAllByLocation(location: TrashSpotLocation): List<TrashSpot>

    fun findAllByLocationAndRegion(
        region: Region,
        location: TrashSpotLocation,
    ): List<TrashSpot>
}
