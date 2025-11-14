package com.sseudam.pet

import com.sseudam.pet.event.NewPetSeasonCreatedEvent
import org.springframework.context.ApplicationEventPublisher
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import java.time.LocalDate
import java.time.Month

@Component
class PetScheduler(
    private val userPetFacade: UserPetFacade,
    private val eventPublisher: ApplicationEventPublisher,
) {
    @Scheduled(cron = "0 0 0 1 * *")
    fun createPetSeason() {
        val nextDay = LocalDate.now().plusDays(1)
        val currentYear = nextDay.year
        val currentMonth = Month.from(nextDay)
        userPetFacade.createBatchUserPet(currentYear, currentMonth)
        eventPublisher.publishEvent(
            NewPetSeasonCreatedEvent(
                year = currentYear,
                month = currentMonth.value,
            ),
        )
    }
}
