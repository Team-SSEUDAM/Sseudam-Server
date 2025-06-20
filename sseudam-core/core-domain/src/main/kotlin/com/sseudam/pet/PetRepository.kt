package com.sseudam.pet

import java.time.Month

interface PetRepository {
    fun findBy(petId: Long): Pet.Info

    fun findAllLatestSeasonPets(
        currentYear: Int,
        currentMonth: Month,
    ): List<Pet.Info>
}
