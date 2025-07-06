package com.sseudam.pet

import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import java.time.LocalDate
import java.time.Month

@Component
class PetScheduler(
    private val petService: PetService,
) {
    @Scheduled(cron = "0 59 23 L * *")
    fun createPetSeason() {
        val currentYear = LocalDate.now().plusDays(1).year
        val currentMonth = Month.from(LocalDate.now().plusDays(1))
        petService.createPetSeason(currentYear, currentMonth)
    }
}
