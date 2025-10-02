package com.sseudam.report.event

import com.sseudam.pet.event.UserPetContextEvent
import org.springframework.context.ApplicationEventPublisher
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component

@Component
class SpotReportPetPointHandler(
    private val applicationEventPublisher: ApplicationEventPublisher,
) {
    @EventListener
    fun handlePetPointReward(event: SpotReportCreatedEvent) {
        applicationEventPublisher.publishEvent(
            UserPetContextEvent(
                userId = event.userId,
                petPointAction = event.petPointAction,
            ),
        )
    }
}
