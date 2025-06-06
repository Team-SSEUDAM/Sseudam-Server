package com.sseudam.suggestion

import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class SuggestionService(
    private val spotSuggestionAppender: SpotSuggestionAppender,
    private val suggestionS3Caller: SuggestionS3Caller,
) {
    fun createSuggestion(suggestion: SpotSuggestion.Create): Pair<SpotSuggestion.Info, SuggestionImageUrl> {
        val createUploadUrl =
            suggestionS3Caller.createUploadUrl(
                suggestion.userId,
                LocalDateTime.now(),
            )
        val spotSuggestion = spotSuggestionAppender.append(createUploadUrl.imageUrl, suggestion)
        return spotSuggestion to createUploadUrl
    }
}
