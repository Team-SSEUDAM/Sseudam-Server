package com.sseudam.suggestion

import com.sseudam.common.ImageS3Caller
import com.sseudam.common.S3ImageUrl
import com.sseudam.pet.PetPointAction
import com.sseudam.suggestion.event.SpotSuggestionCreatedEvent
import com.sseudam.suggestion.result.CreateSpotSuggestionResult
import com.sseudam.support.Cache
import com.sseudam.support.error.ErrorException
import com.sseudam.support.error.ErrorType
import com.sseudam.trashspot.component.TrashSpotReader
import com.sseudam.trashspot.component.TrashSpotValidator
import org.locationtech.jts.geom.Coordinate
import org.locationtech.jts.geom.GeometryFactory
import org.locationtech.jts.geom.PrecisionModel
import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class SuggestionFacade(
    private val suggestionService: SuggestionService,
    private val trashSpotReader: TrashSpotReader,
    private val trashSpotValidator: TrashSpotValidator,
    private val imageS3Caller: ImageS3Caller,
    private val applicationEventPublisher: ApplicationEventPublisher,
) {
    companion object {
        private const val SUGGESTION_IMAGE_PREFIX = "suggestion"
        private const val SUGGESTION_EMPTY_DEFAULT_IMAGE_URL = "https://img.sseudam.me/dev/default_image_trash_spot.webp"
        private val GEOMETRY_FACTORY = GeometryFactory(PrecisionModel(), 4326)
    }

    fun validateSpotSuggestion(name: String): Boolean {
        suggestionService.validateSpotSuggestionName(name)
        if (trashSpotReader.existsByName(name)) {
            throw ErrorException(ErrorType.DUPLICATE_SPOT_NAME)
        }
        return true
    }

    @Transactional
    fun createSpotSuggestion(create: SpotSuggestion.Create): CreateSpotSuggestionResult {
        val point = GEOMETRY_FACTORY.createPoint(Coordinate(create.longitude, create.latitude))
        trashSpotValidator.verifyPoint(point)

        val s3ImageUrl =
            if (create.isPhotoSelected) {
                imageS3Caller.createUploadUrl(create.userId, SUGGESTION_IMAGE_PREFIX)
            } else {
                S3ImageUrl(
                    presignedUrl = "",
                    imageUrl = SUGGESTION_EMPTY_DEFAULT_IMAGE_URL,
                )
            }

        return suggestionService
            .append(create, s3ImageUrl)
            .apply {
                Cache.delete("user:${create.userId}:histories")
            }.also {
                applicationEventPublisher.publishEvent(
                    SpotSuggestionCreatedEvent(
                        userId = create.userId,
                        spotSuggestion = it.suggestionInfo,
                        petPointAction = PetPointAction.SUGGESTION,
                    ),
                )
            }
    }
}
