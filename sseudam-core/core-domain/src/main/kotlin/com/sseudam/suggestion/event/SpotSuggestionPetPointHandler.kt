package com.sseudam.suggestion.event

import com.sseudam.pet.event.UserPetContextEvent
import org.springframework.context.ApplicationEventPublisher
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component

@Component
class SpotSuggestionPetPointHandler(
    private val applicationEventPublisher: ApplicationEventPublisher,
) {
    @EventListener
    fun handlePetPointReward(event: SpotSuggestionCreatedEvent) {
        applicationEventPublisher.publishEvent(
            UserPetContextEvent(
                userId = event.userId,
                petPointAction = event.petPointAction,
            ),
        )
    }
}
