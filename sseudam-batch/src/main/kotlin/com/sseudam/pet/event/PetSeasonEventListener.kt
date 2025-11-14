package com.sseudam.pet.event

import com.sseudam.notification.event.NewPetNotificationRequestedEvent
import org.springframework.context.ApplicationEventPublisher
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component

@Component
class PetSeasonEventListener(
    private val eventPublisher: ApplicationEventPublisher,
) {
    @EventListener
    fun handleNewPetSeasonCreated(event: NewPetSeasonCreatedEvent) {
        eventPublisher.publishEvent(NewPetNotificationRequestedEvent())
    }
}
