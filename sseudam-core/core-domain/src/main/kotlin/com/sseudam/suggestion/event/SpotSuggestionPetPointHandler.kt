package com.sseudam.suggestion.event

import com.sseudam.pet.event.UserPetContextEvent
import org.springframework.context.ApplicationEventPublisher
import org.springframework.modulith.events.ApplicationModuleListener
import org.springframework.stereotype.Component

@Component
class SpotSuggestionPetPointHandler(
    private val applicationEventPublisher: ApplicationEventPublisher,
) {
    @ApplicationModuleListener(id = "spot-suggestion-pet-point-reward")
    fun handlePetPointReward(event: SpotSuggestionCreatedEvent) {
        applicationEventPublisher.publishEvent(
            UserPetContextEvent(
                userId = event.userId,
                petPointAction = event.petPointAction,
            ),
        )
    }
}
