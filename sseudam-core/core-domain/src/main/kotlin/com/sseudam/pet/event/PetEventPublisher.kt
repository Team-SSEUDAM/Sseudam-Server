package com.sseudam.pet.event

import com.sseudam.pet.PetPointAction
import com.sseudam.pet.UserPetService
import com.sseudam.support.error.ErrorException
import com.sseudam.support.error.ErrorType
import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Component

@Component
class PetEventPublisher(
    private val applicationEventPublisher: ApplicationEventPublisher,
    private val userPetService: UserPetService,
) {
    fun publish(
        userId: Long,
        petPointAction: PetPointAction,
    ) {
        val userPet =
            userPetService.findByUser(userId)
                ?: throw ErrorException(ErrorType.NOT_FOUND_DATA)
        applicationEventPublisher.publishEvent(
            PetPointEvent(
                userPet = userPet,
                petPointAction = petPointAction,
            ),
        )
    }
}
