package com.sseudam.admin.presentation.response.suggestion

import com.sseudam.admin.application.suggestion.AdminSpotSuggestionDetail
import com.sseudam.support.page.Page

data class SpotSuggestionAllAdminResponse(
    val list: List<SpotSuggestionAdminResponse>,
    val totalCount: Long,
) {
    companion object {
        fun of(page: Page<AdminSpotSuggestionDetail>) =
            SpotSuggestionAllAdminResponse(
                list = page.content.map { SpotSuggestionAdminResponse.of(it) },
                totalCount = page.totalCount,
            )
    }
}
