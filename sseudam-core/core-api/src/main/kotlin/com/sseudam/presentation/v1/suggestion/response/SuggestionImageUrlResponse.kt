package com.sseudam.presentation.v1.suggestion.response

import com.sseudam.common.S3ImageUrl
import com.sseudam.suggestion.SpotSuggestion
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
            s3ImageUrl: S3ImageUrl,
        ): SuggestionImageUrlResponse = SuggestionImageUrlResponse(suggestion.id, s3ImageUrl.presignedUrl)
    }
}
