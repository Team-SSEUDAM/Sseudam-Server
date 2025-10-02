package com.sseudam.suggestion.event

import com.sseudam.pet.PetPointAction
import com.sseudam.pet.event.UserPetContextEvent
import com.sseudam.suggestion.SuggestionService
import com.sseudam.support.CacheRepository
import com.sseudam.trashspot.TrashSpotService
import com.sseudam.trashspot.image.TrashSpotImage
import com.sseudam.trashspot.image.TrashSpotImageService
import org.springframework.context.ApplicationEventPublisher
import org.springframework.modulith.events.ApplicationModuleListener
import org.springframework.stereotype.Component

@Component
class SuggestionUpdateHandler(
    private val trashSpotService: TrashSpotService,
    private val trashSpotImageService: TrashSpotImageService,
    private val suggestionService: SuggestionService,
    private val cacheRepository: CacheRepository,
    private val applicationEventPublisher: ApplicationEventPublisher,
) {
    companion object {
        private const val SPOT_DETAIL_CACHE_KEY_PREFIX = "spot:detail:"
    }

    @ApplicationModuleListener(
        id = "suggestion-update-approve",
        condition = "#event.suggestion.status.name() == 'APPROVE'",
    )
    fun handleApprove(event: SuggestionUpdateEvent) {
        val trashSpot = trashSpotService.createTrashSpotBySuggestion(event.suggestion)
        trashSpotImageService.append(
            TrashSpotImage.Create(trashSpot.id, event.suggestion.imageUrl),
        )
        applicationEventPublisher.publishEvent(
            UserPetContextEvent(
                userId = event.suggestion.userId,
                petPointAction = PetPointAction.SUGGESTION_APPROVED,
            ),
        )
        cacheRepository.delete(SPOT_DETAIL_CACHE_KEY_PREFIX + trashSpot.id)
    }

    @ApplicationModuleListener(
        id = "suggestion-update-reject-reason",
        condition = "#event.suggestion.status.name() == 'REJECT' && #event.reason != null && #event.reason.trim().length() > 0",
    )
    fun handleReject(event: SuggestionUpdateEvent) {
        val reason = event.reason ?: return
        suggestionService.appendReject(event.suggestion.id, reason)
    }
}
