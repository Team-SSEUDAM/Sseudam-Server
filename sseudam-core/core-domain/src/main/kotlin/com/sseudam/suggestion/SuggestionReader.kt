package com.sseudam.suggestion

import com.sseudam.support.cursor.OffsetPageRequest
import org.springframework.stereotype.Component

@Component
class SuggestionReader(
    private val spotSuggestionRepository: SpotSuggestionRepository,
) {
    fun readBy(suggestionId: Long): SpotSuggestion.Info = spotSuggestionRepository.findBy(suggestionId)

    fun readAllByUser(userId: Long): List<SpotSuggestion.Info> = spotSuggestionRepository.findAllByUserId(userId)

    fun readBySite(site: String): SpotSuggestion.Info? = spotSuggestionRepository.findBySite(site)

    fun readAllBy(
        offsetPageRequest: OffsetPageRequest,
        searchStatus: SuggestionStatus?,
    ): List<SpotSuggestion.Info> = spotSuggestionRepository.findAllBy(offsetPageRequest, searchStatus)
}
