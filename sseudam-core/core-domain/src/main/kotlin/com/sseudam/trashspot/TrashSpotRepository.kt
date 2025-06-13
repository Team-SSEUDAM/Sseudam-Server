package com.sseudam.trashspot

import com.sseudam.support.geo.Region

interface TrashSpotRepository {
    fun save(createTrashSpot: TrashSpot.Create): TrashSpot.Info

    fun findAll(): List<TrashSpot.Info>

    fun findAllByRegion(region: Region): List<TrashSpot.Info>

    fun findAllByLocation(location: TrashSpotLocation): List<TrashSpot.Info>

    fun findAllByLocationAndRegion(
        region: Region,
        location: TrashSpotLocation,
    ): List<TrashSpot.Info>

    fun findAllByType(type: TrashType): List<TrashSpot.Info>

    fun findAllByLocationAndType(
        location: TrashSpotLocation,
        type: TrashType,
    ): List<TrashSpot.Info>

    fun findById(spotId: Long): TrashSpot.Info

    fun findAllByIds(spotIds: List<Long>): List<TrashSpot.Info>
}
