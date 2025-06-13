package com.sseudam.admin.presentation.response.suggestion

data class SpotSuggestionAllResponse(
    val list: List<SpotSuggestionResponse?>,
) {
    companion object {
        fun of(spotSuggestions: List<SpotSuggestionResponse>) = SpotSuggestionAllResponse(spotSuggestions)
    }
}
