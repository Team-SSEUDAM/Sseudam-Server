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
            typeReference = object : TypeReference<List<TrashSpot.Info>>() {},
        ) {
            trashSpotRepository.findAll()
        }

    fun readAllByRegion(region: Region): List<TrashSpot.Info> =
        Cache.cache(
            ttl = 360,
            key = "$ALL_SPOT:${region.name}",
            typeReference = object : TypeReference<List<TrashSpot.Info>>() {},
        ) {
            trashSpotRepository.findAllByRegion(region)
        }

    fun readAllByLocation(location: TrashSpotLocation): List<TrashSpot.Info> = trashSpotRepository.findAllByLocation(location)

    fun readAllByLocationAndRegion(
        region: Region,
        location: TrashSpotLocation,
    ): List<TrashSpot.Info> = trashSpotRepository.findAllByLocationAndRegion(region, location)

    fun readAllByType(type: TrashType): List<TrashSpot.Info> =
        Cache.cache(
            ttl = 360,
            key = "$ALL_SPOT:${type.name}",
            typeReference = object : TypeReference<List<TrashSpot.Info>>() {},
        ) {
            trashSpotRepository.findAllByType(type)
        }

    fun readAllByLocationAndType(
        location: TrashSpotLocation,
        type: TrashType,
    ): List<TrashSpot.Info> = trashSpotRepository.findAllByLocationAndType(location, type)

    fun findByCondition(condition: FindTrashSpotPolicyCondition): List<TrashSpot.Info> =
        when (condition) {
            FindTrashSpotPolicyCondition.All -> readAll()
            is FindTrashSpotPolicyCondition.ByRegion -> readAllByRegion(condition.region)
            is FindTrashSpotPolicyCondition.ByLocation -> readAllByLocation(condition.location)
            is FindTrashSpotPolicyCondition.ByRegionAndLocation -> readAllByLocationAndRegion(condition.region, condition.location)
            is FindTrashSpotPolicyCondition.ByType -> readAllByType(condition.type)
            is FindTrashSpotPolicyCondition.ByTypeAndLocation -> readAllByLocationAndType(condition.location, condition.type)
        }

    fun readBy(spotId: Long): TrashSpot.Info = trashSpotRepository.findById(spotId)

    fun readAllByIds(spotIds: List<Long>): List<TrashSpot.Info> = trashSpotRepository.findAllByIds(spotIds)
}
