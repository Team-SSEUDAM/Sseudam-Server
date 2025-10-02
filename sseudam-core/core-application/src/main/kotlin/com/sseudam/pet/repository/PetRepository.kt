package com.sseudam.pet.repository

import com.sseudam.pet.Pet
import java.time.Month

interface PetRepository {
    fun save(create: Pet.Create): Pet.Info

    fun findBy(petId: Long): Pet.Info

    fun findAllLatestSeasonPets(
        currentYear: Int,
        currentMonth: Month,
    ): List<Pet.Info>
}
