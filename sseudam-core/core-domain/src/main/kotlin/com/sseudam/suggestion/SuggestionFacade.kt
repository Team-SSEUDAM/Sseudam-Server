package com.sseudam.suggestion

import com.sseudam.common.S3ImageUrl
import com.sseudam.pet.PetPointAction
import com.sseudam.suggestion.event.SpotSuggestionCreatedEvent
import com.sseudam.support.Cache
import com.sseudam.support.tx.Tx
import com.sseudam.trashspot.TrashSpotService
import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Service

@Service
class SuggestionFacade(
    private val suggestionService: SuggestionService,
    private val trashSpotService: TrashSpotService,
    private val applicationEventPublisher: ApplicationEventPublisher,
) {
    fun validateSpotSuggestion(name: String): Boolean {
        suggestionService.validateSpotSuggestionName(name)
        trashSpotService.validateSpotName(name)
        return true
    }

    fun createSpotSuggestion(create: SpotSuggestion.Create): Pair<SpotSuggestion.Info, S3ImageUrl> =
        Tx.writeable {
            trashSpotService.appendVerifySpot(create.site, create.longitude, create.latitude)
            return@writeable suggestionService
                .append(create)
                .apply {
                    Cache.delete("user:${create.userId}:histories")
                }.also {
                    applicationEventPublisher.publishEvent(
                        SpotSuggestionCreatedEvent(
                            userId = create.userId,
                            spotSuggestion = it.first,
                            petPointAction = PetPointAction.SUGGESTION,
                        ),
                    )
                }
        }
}
