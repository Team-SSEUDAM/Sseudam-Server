package com.sseudam.suggestion

import org.springframework.stereotype.Component

@Component
class SuggestionReader(
    private val spotSuggestionRepository: SpotSuggestionRepository,
) {
    fun readBySite(site: String): SpotSuggestion.Info? = spotSuggestionRepository.findBySite(site)
}
