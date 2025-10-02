package com.sseudam.storage.db.core.pet

import com.sseudam.pet.Pet
import com.sseudam.pet.repository.PetRepository
import com.sseudam.storage.db.core.support.findByIdOrElseThrow
import com.sseudam.support.tx.Tx
import org.springframework.stereotype.Repository
import java.time.Month

@Repository
class PetCoreRepository(
    private val petJpaRepository: PetJpaRepository,
) : PetRepository {
    override fun save(create: Pet.Create): Pet.Info =
        Tx.writeable {
            petJpaRepository.save(PetEntity(create)).toPetInfo()
        }

    override fun findBy(petId: Long): Pet.Info =
        Tx.readable {
            petJpaRepository
                .findByIdOrElseThrow(petId)
                .toPetInfo()
        }

    override fun findAllLatestSeasonPets(
        currentYear: Int,
        currentMonth: Month,
    ): List<Pet.Info> =
        Tx.readable {
            petJpaRepository
                .findAllByYearAndMonthly(currentYear, currentMonth)
                .map { it.toPetInfo() }
        }
}
