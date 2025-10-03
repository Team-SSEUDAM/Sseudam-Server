package com.sseudam.report.event

import com.sseudam.pet.event.UserPetContextEvent
import org.springframework.context.ApplicationEventPublisher
import org.springframework.modulith.events.ApplicationModuleListener
import org.springframework.stereotype.Component

@Component
class SpotReportPetPointHandler(
    private val applicationEventPublisher: ApplicationEventPublisher,
) {
    @ApplicationModuleListener(id = "spot-report-pet-point-reward")
    fun handlePetPointReward(event: SpotReportCreatedEvent) {
        applicationEventPublisher.publishEvent(
            UserPetContextEvent(
                userId = event.userId,
                petPointAction = event.petPointAction,
            ),
        )
    }
}
