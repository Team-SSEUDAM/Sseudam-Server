package com.sseudam.suggestion.component

import com.sseudam.suggestion.repository.SpotSuggestionRepository
import com.sseudam.support.error.ErrorException
import com.sseudam.support.error.ErrorType
import org.locationtech.jts.geom.Point
import org.springframework.stereotype.Component

@Component
class SuggestionValidator(
    private val suggestionRepository: SpotSuggestionRepository,
) {
    fun verifySite(site: String) {
        val suggestion = suggestionRepository.findBySite(site)
        if (suggestion != null) {
            throw ErrorException(ErrorType.ALREADY_EXIST_SUGGESTION_SPOT_SITE)
        }
    }

    fun verifyPoint(point: Point) {
        val suggestion = suggestionRepository.findByPoint(point)
        if (suggestion != null) {
            throw ErrorException(ErrorType.ALREADY_EXIST_SPOT_POINT)
        }
    }
}
