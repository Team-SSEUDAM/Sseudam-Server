package com.sseudam.trashspot

import com.sseudam.support.geo.Region

sealed class FindTrashSpotPolicyCondition {
    data object All : FindTrashSpotPolicyCondition()

    data class ByRegion(
        val region: Region,
    ) : FindTrashSpotPolicyCondition()

    data class ByLocation(
        val location: TrashSpotLocation,
    ) : FindTrashSpotPolicyCondition()

    data class ByRegionAndLocation(
        val region: Region,
        val location: TrashSpotLocation,
    ) : FindTrashSpotPolicyCondition()
}
