package com.sseudam.visit

import com.sseudam.notification.FcmSender
import com.sseudam.pet.PetPointAction
import com.sseudam.pet.event.PetEventPublisher
import com.sseudam.suggestion.SuggestionService
import com.sseudam.trashspot.TrashSpotService
import com.sseudam.user.device.UserDeviceService
import org.springframework.stereotype.Service

@Service
class SpotVisitedFacade(
    private val spotVisitedService: SpotVisitedService,
    private val trashSpotService: TrashSpotService,
    private val petEventPublisher: PetEventPublisher,
    private val suggestionService: SuggestionService,
    private val userDeviceService: UserDeviceService,
    private val fcmSender: FcmSender,
) {
    fun visitSpot(
        userId: Long,
        spotId: Long,
    ) {
        val visited = spotVisitedService.append(SpotVisited.Create(userId, spotId))
        petEventPublisher.publish(visited.userId, PetPointAction.SPOT_VISITED)

        val suggestion = suggestionService.findSpotSuggestionBySpotId(spotId)

        val userDevices = userDeviceService.findByUserId(visited.userId)
    }

    fun findSpotVisitedByUserId(userId: Long): SpotVisitedAll {
        val spotVisited = spotVisitedService.findAllByUser(userId)
        if (spotVisited.isEmpty()) return SpotVisitedAll(emptyList())

        val spotIds = spotVisited.map { it.spotId }
        val spots = trashSpotService.findAllByIds(spotIds)
        return SpotVisitedAll.of(spotVisited, spots)
    }
}
