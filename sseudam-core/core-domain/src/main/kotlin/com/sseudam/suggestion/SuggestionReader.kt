package com.sseudam.suggestion

import com.sseudam.support.cursor.OffsetPageRequest
import com.sseudam.support.page.Page
import org.springframework.stereotype.Component

@Component
class SuggestionReader(
    private val spotSuggestionRepository: SpotSuggestionRepository,
) {
    fun readBy(suggestionId: Long): SpotSuggestion.Info = spotSuggestionRepository.findBy(suggestionId)

    fun readAllByUser(userId: Long): List<SpotSuggestion.Info> = spotSuggestionRepository.findAllByUserId(userId)

    fun readBySite(site: String): SpotSuggestion.Info? = spotSuggestionRepository.findBySite(site)

    fun readLastBySpotId(spotId: Long): SpotSuggestion.Info? = spotSuggestionRepository.findLastBySpotId(spotId)

    fun readAllBy(
        offsetPageRequest: OffsetPageRequest,
        searchStatus: SuggestionStatus?,
    ): Page<SpotSuggestion.Info> = spotSuggestionRepository.findAllBy(offsetPageRequest, searchStatus)

    fun existsByName(name: String): Boolean = spotSuggestionRepository.existsByName(name)
}
