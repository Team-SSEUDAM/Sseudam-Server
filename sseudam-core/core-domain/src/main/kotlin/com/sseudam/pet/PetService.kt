package com.sseudam.pet

import org.springframework.stereotype.Service
import java.time.Month

@Service
class PetService(
    private val petReader: PetReader,
    private val petAppender: PetAppender,
) {
    fun findBy(petId: Long): Pet.Info = petReader.readBy(petId)

    fun findAllLatestSeasonPets(
        currentYear: Int,
        currentMonth: Month,
    ): List<Pet.Info> = petReader.readAllLatestSeasonPets(currentYear, currentMonth)
}
