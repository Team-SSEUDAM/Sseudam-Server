package com.sseudam.pet

import org.springframework.stereotype.Component
import java.time.Month

@Component
class PetReader(
    private val petRepository: PetRepository,
) {
    fun readBy(petId: Long): Pet.Info = petRepository.findBy(petId)

    fun readAllLatestSeasonPets(
        currentYear: Int,
        currentMonth: Month,
    ): List<Pet.Info> = petRepository.findAllLatestSeasonPets(currentYear, currentMonth)
}
