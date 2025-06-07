package com.sseudam.visit

import com.sseudam.trashspot.TrashSpot

data class SpotVisitedAll(
    val list: List<SpotVisited.Info>,
) {
    companion object {
        fun of(
            spotVisited: List<SpotVisited.Info>,
            spots: List<TrashSpot>,
        ): SpotVisitedAll {
            val spotsMap = spots.associateBy { it.id }

            return SpotVisitedAll(
                spotVisited.map {
                    it.copy(site = spotsMap[it.spotId]!!.address.site)
                },
            )
        }
    }
}
