package com.sseudam.visit

import com.sseudam.trashspot.TrashSpotService
import org.springframework.stereotype.Service

@Service
class SpotVisitedFacade(
    private val spotVisitedService: SpotVisitedService,
    private val trashSpotService: TrashSpotService,
) {
    fun findSpotVisitedByUserId(userId: Long): SpotVisitedAll {
        val spotVisited = spotVisitedService.findAllByUser(userId)
        if (spotVisited.isEmpty()) return SpotVisitedAll(emptyList())

        val spotIds = spotVisited.map { it.spotId }
        val spots = trashSpotService.findAllByIds(spotIds)
        return SpotVisitedAll.of(spotVisited, spots)
    }
}
