package com.sseudam.pet.event

import com.sseudam.pet.Pet
import com.sseudam.pet.PetPointAction
import com.sseudam.pet.PetService
import com.sseudam.pet.UserPetService
import com.sseudam.support.error.ErrorException
import com.sseudam.support.error.ErrorType
import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Component
import java.time.LocalDate

@Component
class PetEventPublisher(
    private val applicationEventPublisher: ApplicationEventPublisher,
    private val petService: PetService,
    private val userPetService: UserPetService,
) {
    fun publish(
        userId: Long,
        petPointAction: PetPointAction,
    ) {
        val userPet =
            userPetService.findByUser(userId)
                ?: run {
                    val (currentYear, currentMonth) = LocalDate.now().let { it.year to it.month }
                    val pets = petService.findAllLatestSeasonPets(currentYear, currentMonth)
                    val level1Pet =
                        pets.find { it.levelType == Pet.LevelType.LEVEL_1 }
                            ?: throw ErrorException(ErrorType.INVALID_PET_LEVEL_TYPE)
                    userPetService.append(userId, level1Pet)
                }
        applicationEventPublisher.publishEvent(
            PetPointEvent(
                userPet = userPet,
                petPointAction = petPointAction,
            ),
        )
    }
}
