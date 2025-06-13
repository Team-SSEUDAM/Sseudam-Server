package com.sseudam.suggestion.event

import com.sseudam.suggestion.SpotSuggestion
import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Component

@Component
class SuggestionEventPublisher(
    private val applicationEventPublisher: ApplicationEventPublisher,
) {
    fun publish(suggestion: SpotSuggestion.Info) {
        applicationEventPublisher.publishEvent(
            SuggestionUpdateEvent(
                suggestion,
            ),
        )
    }
}
