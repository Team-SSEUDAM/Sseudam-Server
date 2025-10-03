package com.sseudam.suggestion.component

import com.sseudam.suggestion.repository.SpotSuggestionRepository
import org.springframework.stereotype.Component

@Component
class SuggestionDeleter(
    private val spotSuggestionRepository: SpotSuggestionRepository,
) {
    fun deleteBy(suggestionId: Long) {
        spotSuggestionRepository.deleteBy(suggestionId)
    }
}
