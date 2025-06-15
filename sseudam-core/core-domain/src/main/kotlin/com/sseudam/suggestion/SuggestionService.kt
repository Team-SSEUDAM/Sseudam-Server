package com.sseudam.suggestion

import com.sseudam.common.ImageS3Caller
import com.sseudam.common.S3ImageUrl
import com.sseudam.suggestion.event.SuggestionEventPublisher
import com.sseudam.support.cursor.OffsetPageRequest
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class SuggestionService(
    private val suggestionAppender: SuggestionAppender,
    private val suggestionReader: SuggestionReader,
    private val suggestionUpdater: SuggestionUpdater,
    private val suggestionEventPublisher: SuggestionEventPublisher,
    private val imageS3Caller: ImageS3Caller,
) {
    companion object {
        const val SUGGESTION_IMAGE_PATH = "suggestion"
    }

    fun createSpotSuggestion(suggestion: SpotSuggestion.Create): Pair<SpotSuggestion.Info, S3ImageUrl> {
        val createUploadUrl =
            imageS3Caller.createUploadUrl(
                suggestion.userId,
                LocalDateTime.now(),
                SUGGESTION_IMAGE_PATH,
            )
        val spotSuggestion = suggestionAppender.append(createUploadUrl.imageUrl, suggestion)
        return spotSuggestion to createUploadUrl
    }

    fun findAllSpotSuggestionByUser(userId: Long): List<SpotSuggestion.Info> = suggestionReader.readAllByUser(userId)

    fun findSpotSuggestionBySite(site: String): SpotSuggestion.Info? = suggestionReader.readBySite(site)

    fun findSuggestionsBy(
        offsetPageRequest: OffsetPageRequest,
        searchStatus: SuggestionStatus?,
    ): List<SpotSuggestion.Info> = suggestionReader.readAllBy(offsetPageRequest, searchStatus)

    fun findSpotSuggestionById(suggestionId: Long): SpotSuggestion.Info = suggestionReader.readBy(suggestionId)

    fun updateSuggestion(
        suggestionId: Long,
        status: SuggestionStatus,
    ): SpotSuggestion.Info {
        val suggestion = suggestionUpdater.update(suggestionId, status)
        if (suggestion.status == SuggestionStatus.APPROVE) {
            suggestionEventPublisher.publish(suggestion)
        }
        return suggestion
    }
}
