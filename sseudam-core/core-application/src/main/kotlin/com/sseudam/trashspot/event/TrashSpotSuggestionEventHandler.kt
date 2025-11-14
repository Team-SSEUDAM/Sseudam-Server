package com.sseudam.trashspot.event

import com.sseudam.pet.PetPointAction
import com.sseudam.pet.event.UserPetContextEvent
import com.sseudam.suggestion.event.SuggestionUpdateEvent
import com.sseudam.support.Cache
import com.sseudam.trashspot.TrashSpotService
import com.sseudam.trashspot.image.TrashSpotImage
import com.sseudam.trashspot.image.TrashSpotImageService
import org.springframework.context.ApplicationEventPublisher
import org.springframework.modulith.events.ApplicationModuleListener
import org.springframework.stereotype.Component

@Component
class TrashSpotSuggestionEventHandler(
    private val trashSpotService: TrashSpotService,
    private val trashSpotImageService: TrashSpotImageService,
    private val applicationEventPublisher: ApplicationEventPublisher,
) {
    companion object {
        private const val SPOT_DETAIL_CACHE_KEY_PREFIX = "spot:detail:"
    }

    @ApplicationModuleListener(
        id = "trashspot-create-from-suggestion",
        condition = "#event.suggestion.status.name() == 'APPROVE'",
    )
    fun handleApprovedSuggestion(event: SuggestionUpdateEvent) {
        val trashSpot = trashSpotService.createTrashSpotBySuggestion(event.suggestion)
        trashSpotImageService.append(
            TrashSpotImage.Create(trashSpot.id, event.suggestion.imageUrl),
        )
        Cache.delete(SPOT_DETAIL_CACHE_KEY_PREFIX + trashSpot.id)
        Cache.delete("user:${event.suggestion.userId}:histories")
        applicationEventPublisher.publishEvent(
            UserPetContextEvent(
                userId = event.suggestion.userId,
                petPointAction = PetPointAction.SUGGESTION_APPROVED,
            ),
        )
    }
}
