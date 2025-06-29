package com.sseudam.suggestion

import com.sseudam.trashspot.TrashSpotService
import org.springframework.stereotype.Service

@Service
class SuggestionFacade(
    private val suggestionService: SuggestionService,
    private val trashSpotService: TrashSpotService,
) {
    fun validateSpotSuggestion(name: String): Boolean {
        suggestionService.validateSpotSuggestionName(name)
        trashSpotService.validateSpotName(name)

        return true
    }
}
