package com.sseudam.storage.db.core.pet

import com.sseudam.pet.Pet
import com.sseudam.pet.PetRepository
import com.sseudam.storage.db.core.support.findByIdOrElseThrow
import com.sseudam.support.tx.TxAdvice
import org.springframework.stereotype.Repository
import java.time.Month

@Repository
class PetCoreRepository(
    private val petJpaRepository: PetJpaRepository,
    private val txAdvice: TxAdvice,
) : PetRepository {
    override fun findBy(petId: Long): Pet.Info =
        txAdvice.readOnly {
            petJpaRepository
                .findByIdOrElseThrow(petId)
                .toPetInfo()
        }

    override fun findAllLatestSeasonPets(
        currentYear: Int,
        currentMonth: Month,
    ): List<Pet.Info> =
        txAdvice.readOnly {
            petJpaRepository
                .findAllByYearAndMonthly(currentYear, currentMonth)
                .map { it.toPetInfo() }
        }
}
