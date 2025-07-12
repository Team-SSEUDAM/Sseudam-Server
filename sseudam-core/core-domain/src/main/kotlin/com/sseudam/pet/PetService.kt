package com.sseudam.pet

import com.sseudam.support.error.ErrorException
import com.sseudam.support.error.ErrorType
import org.springframework.stereotype.Service
import java.time.Month

@Service
class PetService(
    private val petReader: PetReader,
    private val petAppender: PetAppender,
) {
    fun createPetSeason(
        currentYear: Int,
        currentMonth: Month,
    ): Pet.Info {
        val petInfos =
            Pet.LevelType.entries.map {
                petAppender.appendSeasonPet(
                    Pet.Create(
                        name = "냥이",
                        levelType = it,
                        year = currentYear,
                        monthly = currentMonth,
                    ),
                )
            }
        return petInfos.firstOrNull() ?: throw ErrorException(ErrorType.FAILED_PET_CREATION)
    }

    fun findBy(petId: Long): Pet.Info = petReader.readBy(petId)

    fun findAllLatestSeasonPets(
        currentYear: Int,
        currentMonth: Month,
    ): List<Pet.Info> = petReader.readAllLatestSeasonPets(currentYear, currentMonth)
}
