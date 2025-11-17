package com.sseudam.trashspot.component

import com.sseudam.common.Region
import com.sseudam.common.TrashType
import com.sseudam.trashspot.dto.TrashSpotLocation

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

    data class ByType(
        val type: TrashType,
    ) : FindTrashSpotPolicyCondition()

    data class ByTypeAndLocation(
        val type: TrashType,
        val location: TrashSpotLocation,
    ) : FindTrashSpotPolicyCondition()
}
