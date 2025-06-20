package com.sseudam.storage.db.core.pet

import com.sseudam.pet.Pet
import com.sseudam.pet.PetRepository
import org.springframework.stereotype.Repository
import java.time.Month

@Repository
class PetCoreRepository(
    private val petJpaRepository: PetJpaRepository,
) : PetRepository {
    override fun findAllLatestSeasonPets(
        currentYear: Int,
        currentMonth: Month,
    ): List<Pet.Info> =
        petJpaRepository
            .findAllByYearAndMonthly(currentYear, currentMonth)
            .map { it.toPetInfo() }
}
