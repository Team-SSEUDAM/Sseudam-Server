package com.sseudam.admin.presentation.response.suggestion

import com.sseudam.suggestion.SpotSuggestion
import com.sseudam.support.page.Page

data class SpotSuggestionAllResponse(
    val list: List<SpotSuggestionResponse>,
    val totalCount: Long,
) {
    companion object {
        fun of(page: Page<SpotSuggestion.Info>) =
            SpotSuggestionAllResponse(
                list = page.content.map { SpotSuggestionResponse.of(it) },
                totalCount = page.totalCount,
            )
    }
}
