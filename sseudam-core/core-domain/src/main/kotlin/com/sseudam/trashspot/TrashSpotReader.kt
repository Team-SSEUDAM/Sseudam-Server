package com.sseudam.trashspot

import com.fasterxml.jackson.core.type.TypeReference
import com.sseudam.support.Cache
import com.sseudam.support.geo.Region
import org.springframework.stereotype.Component

@Component
class TrashSpotReader(
    private val trashSpotRepository: TrashSpotRepository,
) {
    companion object {
        val ALL_SPOT = "trash:spot:all"
    }

    fun readAll() =
        Cache.cache(
            ttl = 360,
            key = ALL_SPOT,
            typeReference = object : TypeReference<List<TrashSpot>>() {},
        ) {
            trashSpotRepository.findAll()
        }

    fun readAllByRegion(region: Region): List<TrashSpot> =
        Cache.cache(
            ttl = 360,
            key = "$ALL_SPOT:${region.name}",
            typeReference = object : TypeReference<List<TrashSpot>>() {},
        ) {
            trashSpotRepository.findAllByRegion(region)
        }

    fun readAllByLocation(location: TrashSpotLocation): List<TrashSpot> = trashSpotRepository.findAllByLocation(location)

    fun readAllByLocationAndRegion(
        region: Region,
        location: TrashSpotLocation,
    ): List<TrashSpot> = trashSpotRepository.findAllByLocationAndRegion(region, location)

    fun findByCondition(condition: FindTrashSpotPolicyCondition): List<TrashSpot> =
        when (condition) {
            FindTrashSpotPolicyCondition.All -> readAll()
            is FindTrashSpotPolicyCondition.ByRegion -> readAllByRegion(condition.region)
            is FindTrashSpotPolicyCondition.ByLocation -> readAllByLocation(condition.location)
            is FindTrashSpotPolicyCondition.ByRegionAndLocation -> readAllByLocationAndRegion(condition.region, condition.location)
        }

    fun readBy(spotId: Long): TrashSpot = trashSpotRepository.findById(spotId)
}
