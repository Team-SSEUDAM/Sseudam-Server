package com.sseudam.suggestion

import com.sseudam.support.error.ErrorException
import com.sseudam.support.error.ErrorType
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
}
