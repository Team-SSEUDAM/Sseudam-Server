package com.sseudam.pet.event

import com.sseudam.notification.NotificationFacade
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component

@Component
class PetSeasonEventListener(
    private val notificationFacade: NotificationFacade,
) {
    @EventListener
    fun handleNewPetSeasonCreated(event: NewPetSeasonCreatedEvent) {
        notificationFacade.sendNewPetNotifications()
    }
}
