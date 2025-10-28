package com.sseudam.suggestion.result

import com.sseudam.suggestion.SpotSuggestion

data class CreateSpotSuggestionResult(
    val suggestionInfo: SpotSuggestion.Info,
    val presignedUrl: String,
)
