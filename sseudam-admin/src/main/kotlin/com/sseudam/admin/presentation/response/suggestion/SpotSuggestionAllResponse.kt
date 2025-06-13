package com.sseudam.admin.presentation.response.suggestion

import com.sseudam.suggestion.SpotSuggestion

data class SpotSuggestionAllResponse(
    val list: List<SpotSuggestionResponse>,
) {
    companion object {
        fun of(spotSuggestions: List<SpotSuggestion.Info>) =
            SpotSuggestionAllResponse(
                spotSuggestions.map { SpotSuggestionResponse.of(it) },
            )
    }
}
