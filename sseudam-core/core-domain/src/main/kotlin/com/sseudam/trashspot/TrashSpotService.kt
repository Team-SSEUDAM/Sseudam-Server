package com.sseudam.trashspot

import com.sseudam.support.geo.Region
import org.springframework.stereotype.Service

@Service
class TrashSpotService(
    private val trashSpotReader: TrashSpotReader,
) {
    fun findAll(
        region: Region?,
        location: TrashSpotLocation,
    ): List<TrashSpot> {
        val condition =
            when {
                !location.isNotSet() && region == null -> FindTrashSpotPolicyCondition.All
                !location.isNotSet() -> FindTrashSpotPolicyCondition.ByRegion(region!!)
                region == null -> FindTrashSpotPolicyCondition.ByLocation(location)
                else -> FindTrashSpotPolicyCondition.ByRegionAndLocation(region, location)
            }
        return trashSpotReader.findByCondition(condition)
    }
}
