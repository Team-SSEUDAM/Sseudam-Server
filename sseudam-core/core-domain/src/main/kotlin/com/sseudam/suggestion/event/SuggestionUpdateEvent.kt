package com.sseudam.suggestion.event

import com.sseudam.suggestion.SpotSuggestion

data class SuggestionUpdateEvent(
    val suggestion: SpotSuggestion.Info,
)
