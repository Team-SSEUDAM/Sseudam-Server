package com.sseudam.presentation.v1.suggestion.response

import com.sseudam.suggestion.SpotSuggestion
import com.sseudam.suggestion.SuggestionImageUrl
import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "제보하기 PresignedUrl 응답 Json")
data class SuggestionImageUrlResponse(
    @Schema(description = "제보 ID")
    val suggestionId: Long,
    @Schema(description = "PresignedUrl")
    val presignedUrl: String,
) {
    companion object {
        fun of(
            suggestion: SpotSuggestion.Info,
            suggestionImageUrl: SuggestionImageUrl,
        ): SuggestionImageUrlResponse = SuggestionImageUrlResponse(suggestion.id, suggestionImageUrl.presignedUrl)
    }
}
