package com.sseudam.suggestion

import org.springframework.stereotype.Component

@Component
class SuggestionUpdater(
    private val spotSuggestionRepository: SpotSuggestionRepository,
) {
    fun update(
        suggestionId: Long,
        status: SuggestionStatus,
    ): SpotSuggestion.Info = spotSuggestionRepository.update(suggestionId, status)
}
