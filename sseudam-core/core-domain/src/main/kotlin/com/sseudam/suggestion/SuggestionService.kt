package com.sseudam.suggestion

import com.sseudam.common.ImageS3Caller
import com.sseudam.common.S3ImageUrl
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class SuggestionService(
    private val spotSuggestionAppender: SpotSuggestionAppender,
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
        val spotSuggestion = spotSuggestionAppender.append(createUploadUrl.imageUrl, suggestion)
        return spotSuggestion to createUploadUrl
    }
}
