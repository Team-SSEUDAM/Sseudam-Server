package com.sseudam.suggestion.event

import com.sseudam.contract.suggestion.SuggestionUpdateEvent
import com.sseudam.suggestion.SuggestionService
import org.springframework.modulith.events.ApplicationModuleListener
import org.springframework.stereotype.Component

@Component
class SuggestionUpdateHandler(
    private val suggestionService: SuggestionService,
) {
    @ApplicationModuleListener(
        id = "suggestion-update-reject-reason",
        condition = "#event.suggestion.status == 'REJECT' && #event.reason != null && #event.reason.trim().length() > 0",
    )
    fun handleReject(event: SuggestionUpdateEvent) {
        val reason = event.reason ?: return
        suggestionService.appendReject(event.suggestion.id, reason)
    }
}
