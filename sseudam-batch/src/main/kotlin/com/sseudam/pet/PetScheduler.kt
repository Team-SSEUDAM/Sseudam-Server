package com.sseudam.pet

import com.sseudam.notification.NotificationFacade
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import java.time.LocalDate
import java.time.Month

@Component
class PetScheduler(
    private val userPetFacade: UserPetFacade,
    private val notificationFacade: NotificationFacade,
) {
    @Scheduled(cron = "0 0 0 1 * *")
    fun createPetSeason() {
        val nextDay = LocalDate.now().plusDays(1)
        val currentYear = nextDay.year
        val currentMonth = Month.from(nextDay)
        userPetFacade.createBatchUserPet(currentYear, currentMonth)
        notificationFacade.sendNewPetNotifications()
    }
}
