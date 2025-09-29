package com.sseudam.admin.presentation.response.suggestion

import com.sseudam.suggestion.SpotSuggestion
import com.sseudam.support.page.Page

data class SpotSuggestionAllAdminResponse(
    val list: List<SpotSuggestionAdminResponse>,
    val totalCount: Long,
) {
    companion object {
        fun of(page: Page<SpotSuggestion.Detail>) =
            SpotSuggestionAllAdminResponse(
                list = page.content.map { SpotSuggestionAdminResponse.of(it) },
                totalCount = page.totalCount,
            )
    }
}
