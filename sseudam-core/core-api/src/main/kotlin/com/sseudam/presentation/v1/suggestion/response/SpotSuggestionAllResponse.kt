package com.sseudam.presentation.v1.suggestion.response

import com.sseudam.suggestion.SpotSuggestion
import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "사용자 제보 내역 응답 Json")
data class SpotSuggestionAllResponse(
    val list: List<SpotSuggestionResponse>,
) {
    companion object {
        fun of(spots: List<SpotSuggestion.Info>): SpotSuggestionAllResponse =
            SpotSuggestionAllResponse(spots.map { SpotSuggestionResponse.of(it) })
    }
}
