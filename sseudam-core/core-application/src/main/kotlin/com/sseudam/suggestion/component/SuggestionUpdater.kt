package com.sseudam.suggestion.component

import com.sseudam.suggestion.SpotSuggestion
import com.sseudam.suggestion.SuggestionStatus
import com.sseudam.suggestion.repository.SpotSuggestionRepository
import org.springframework.stereotype.Component

@Component
class SuggestionUpdater(
    private val spotSuggestionRepository: SpotSuggestionRepository,
) {
    fun update(
        suggestionId: Long,
        status: SuggestionStatus,
    ): SpotSuggestion.Info = spotSuggestionRepository.update(suggestionId, status)

    fun cancel(suggestionId: Long) = spotSuggestionRepository.cancel(suggestionId)
}
