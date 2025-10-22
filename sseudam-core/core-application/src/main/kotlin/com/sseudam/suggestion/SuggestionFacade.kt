package com.sseudam.suggestion

import com.sseudam.common.ImageS3Caller
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
    private val imageS3Caller: ImageS3Caller,
    private val applicationEventPublisher: ApplicationEventPublisher,
) {
    companion object {
        private const val SUGGESTION_IMAGE_PREFIX = "suggestion"
    }

    fun validateSpotSuggestion(name: String): Boolean {
        suggestionService.validateSpotSuggestionName(name)
        trashSpotService.validateSpotName(name)
        return true
    }

    fun createSpotSuggestion(create: SpotSuggestion.Create): Pair<SpotSuggestion.Info, S3ImageUrl> =
        Tx.writeable {
            trashSpotService.appendVerifySpot(create.site, create.longitude, create.latitude)
            val uploadUrl = imageS3Caller.createUploadUrl(create.userId, SUGGESTION_IMAGE_PREFIX)

            return@writeable suggestionService
                .append(create, uploadUrl)
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
