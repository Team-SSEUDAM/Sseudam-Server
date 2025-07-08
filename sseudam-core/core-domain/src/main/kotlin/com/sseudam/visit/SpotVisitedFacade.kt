package com.sseudam.visit

import com.sseudam.notification.FcmSender
import com.sseudam.notification.NotificationMessages
import com.sseudam.notification.SendNotificationMessage
import com.sseudam.pet.PetPointAction
import com.sseudam.pet.event.PetEventPublisher
import com.sseudam.suggestion.SuggestionService
import com.sseudam.trashspot.TrashSpotService
import com.sseudam.user.UserService
import org.springframework.stereotype.Service

@Service
class SpotVisitedFacade(
    private val spotVisitedService: SpotVisitedService,
    private val trashSpotService: TrashSpotService,
    private val petEventPublisher: PetEventPublisher,
    private val suggestionService: SuggestionService,
    private val userService: UserService,
    private val fcmSender: FcmSender,
) {
    fun visitSpot(
        userId: Long,
        spotId: Long,
    ) {
        val spot = trashSpotService.findBy(spotId)
        val visited = spotVisitedService.append(SpotVisited.Create(userId, spot.id))
        petEventPublisher.publish(visited.userId, PetPointAction.SPOT_VISITED)

        val suggestion = suggestionService.findSpotSuggestionBySite(spot.address.site) ?: return
        val profile = userService.getProfile(suggestion.userId)
        fcmSender.send(
            SendNotificationMessage(
                userId = suggestion.userId,
                title = NotificationMessages.DEFAULT_TITLE,
                body = NotificationMessages.anonymousVisitedSpotContents(profile.nickname),
            ),
        )
    }

    fun findSpotVisitedByUserId(userId: Long): SpotVisitedAll {
        val spotVisited = spotVisitedService.findAllByUser(userId)
        if (spotVisited.isEmpty()) return SpotVisitedAll(emptyList())

        val spotIds = spotVisited.map { it.spotId }
        val spots = trashSpotService.findAllByIds(spotIds)
        return SpotVisitedAll.of(spotVisited, spots)
    }
}
