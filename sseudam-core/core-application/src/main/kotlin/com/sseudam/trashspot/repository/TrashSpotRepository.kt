package com.sseudam.trashspot.repository

import com.sseudam.common.Region
import com.sseudam.trashspot.TrashSpot
import com.sseudam.trashspot.TrashType
import com.sseudam.trashspot.dto.TrashSpotLocation
import org.locationtech.jts.geom.Point

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

    fun findBySite(site: String): TrashSpot.Info?

    fun findByPoint(point: Point): TrashSpot.Info?

    fun updateName(
        spotId: Long,
        name: String,
    )

    fun updateType(
        spotId: Long,
        type: TrashType,
    )

    fun updateLocation(
        spotId: Long,
        region: Region,
        point: Point,
    )

    fun existsByName(name: String): Boolean
}
