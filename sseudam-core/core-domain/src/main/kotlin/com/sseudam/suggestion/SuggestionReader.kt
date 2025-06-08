package com.sseudam.suggestion

import org.springframework.stereotype.Component

@Component
class SuggestionReader(
    private val spotSuggestionRepository: SpotSuggestionRepository,
) {
    fun readAllByUser(userId: Long): List<SpotSuggestion.Info> = spotSuggestionRepository.findAllByUserId(userId)

    fun readBySite(site: String): SpotSuggestion.Info? = spotSuggestionRepository.findBySite(site)
}
