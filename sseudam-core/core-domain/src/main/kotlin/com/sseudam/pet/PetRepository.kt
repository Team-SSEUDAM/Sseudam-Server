package com.sseudam.pet

import java.time.Month

interface PetRepository {
    fun findAllLatestSeasonPets(
        currentYear: Int,
        currentMonth: Month,
    ): List<Pet.Info>
}
