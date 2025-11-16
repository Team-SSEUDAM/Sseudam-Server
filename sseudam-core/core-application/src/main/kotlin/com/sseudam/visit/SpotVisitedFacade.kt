package com.sseudam.visit

import com.sseudam.contract.trashspot.TrashSpotQueryContract
import com.sseudam.pet.PetPointAction
import com.sseudam.visit.event.SpotVisitedEvent
import com.sseudam.visit.result.SpotVisitedResult
import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate

@Service
class SpotVisitedFacade(
    private val spotVisitedService: SpotVisitedService,
    private val trashSpotService: TrashSpotQueryContract,
    private val applicationEventPublisher: ApplicationEventPublisher,
) {
    @Transactional
    fun visitSpot(
        userId: Long,
        spotId: Long,
        suggesterId: Long?,
    ): SpotVisitedResult {
        val todayVisited = spotVisitedService.findTodaySpotVisitedByUser(userId)
        val todayVisitedSpot = todayVisited.find { it.spotId == spotId }

        spotVisitedService.verifyVisited(todayVisited, todayVisitedSpot)

        val isToday = todayVisitedSpot == null && todayVisited.isEmpty()
        val visited = spotVisitedService.append(SpotVisited.Create(userId, spotId, LocalDate.now()))

        val action = if (isToday) PetPointAction.TODAY_FIRST_SPOT_VISITED else PetPointAction.SPOT_VISITED
        applicationEventPublisher.publishEvent(
            SpotVisitedEvent(
                spotId = spotId,
                suggesterId = suggesterId,
                userId = userId,
                petPointAction = action,
            ),
        )

        return SpotVisitedResult(isToday, visited)
    }

    fun findSpotVisitedByUserId(userId: Long): List<SpotVisited.Info> {
        val spotVisited = spotVisitedService.findAllByUser(userId)
        if (spotVisited.isEmpty()) return emptyList()

        val spotIds = spotVisited.map { it.spotId }
        val spots = trashSpotService.findAllByIds(spotIds)
        val spotsMap = spots.associateBy { it.id }

        return spotVisited
            .mapNotNull { visited ->
                spotsMap[visited.spotId]?.let { spot ->
                    visited.copy(site = spot.address.site)
                }
            }.sortedByDescending { it.visitedAt }
    }
}
