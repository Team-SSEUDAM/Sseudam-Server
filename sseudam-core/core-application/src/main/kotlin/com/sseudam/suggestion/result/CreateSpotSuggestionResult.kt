package com.sseudam.suggestion.result

import com.sseudam.common.S3ImageUrl
import com.sseudam.suggestion.SpotSuggestion

data class CreateSpotSuggestionResult(
    val suggestionInfo: SpotSuggestion.Info,
    val uploadUrl: S3ImageUrl,
)
