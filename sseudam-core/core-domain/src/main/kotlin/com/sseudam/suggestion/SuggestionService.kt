package com.sseudam.suggestion

import com.sseudam.common.ImageS3Caller
import com.sseudam.common.S3ImageUrl
import com.sseudam.pet.PetPointAction
import com.sseudam.pet.event.PetEventPublisher
import com.sseudam.suggestion.event.SuggestionEventPublisher
import com.sseudam.support.cursor.OffsetPageRequest
import com.sseudam.support.error.ErrorException
import com.sseudam.support.error.ErrorType
import com.sseudam.support.page.Page
import com.sseudam.support.tx.TxAdvice
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class SuggestionService(
    private val suggestionAppender: SuggestionAppender,
    private val suggestionReader: SuggestionReader,
    private val suggestionUpdater: SuggestionUpdater,
    private val suggestionDeleter: SuggestionDeleter,
    private val suggestionValidator: SuggestionValidator,
    private val suggestionEventPublisher: SuggestionEventPublisher,
    private val txAdvice: TxAdvice,
    private val petEventPublisher: PetEventPublisher,
    private val imageS3Caller: ImageS3Caller,
) {
    companion object {
        private const val SUGGESTION_IMAGE_PATH = "suggestion"
    }

    fun append(create: SpotSuggestion.Create): Pair<SpotSuggestion.Info, S3ImageUrl> {
        suggestionValidator.verifySite(create.site)

        val uploadUrl = imageS3Caller.createUploadUrl(create.userId, LocalDateTime.now(), SUGGESTION_IMAGE_PATH)
        val spotSuggestion = suggestionAppender.append(uploadUrl.imageUrl, create)
        petEventPublisher.publish(create.userId, PetPointAction.SUGGESTION)
        return spotSuggestion to uploadUrl
    }

    fun findAllSpotSuggestionByUser(userId: Long): List<SpotSuggestion.Info> = suggestionReader.readAllByUser(userId)

    fun findSpotSuggestionBySite(site: String): SpotSuggestion.Info? = suggestionReader.readBySite(site)

    fun findSuggestionsBy(
        offsetPageRequest: OffsetPageRequest,
        searchStatus: SuggestionStatus?,
    ): Page<SpotSuggestion.Info> = suggestionReader.readAllBy(offsetPageRequest, searchStatus)

    fun findSpotSuggestionById(suggestionId: Long): SpotSuggestion.Info = suggestionReader.readBy(suggestionId)

    fun updateSuggestion(
        suggestionId: Long,
        status: SuggestionStatus,
    ): SpotSuggestion.Info =
        txAdvice.write {
            val suggestion = suggestionUpdater.update(suggestionId, status)
            suggestionEventPublisher.publish(suggestion)
            if (suggestion.status == SuggestionStatus.APPROVE) {
                suggestionDeleter.deleteBy(suggestionId)
                petEventPublisher.publish(suggestion.userId, PetPointAction.SUGGESTION_APPROVED)
            }
            return@write suggestion
        }

    fun validateSpotSuggestionName(name: String) {
        if (suggestionReader.existsByName(name)) {
            throw ErrorException(ErrorType.DUPLICATE_SPOT_NAME)
        }
    }
}
