package com.sseudam.pet.event

import com.sseudam.pet.PetPointAction
import com.sseudam.pet.UserPet
import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Component

@Component
class PetEventPublisher(
    private val applicationEventPublisher: ApplicationEventPublisher,
) {
    fun publish(
        userPet: UserPet.Info,
        petPointAction: PetPointAction,
    ) {
        applicationEventPublisher.publishEvent(
            PetPointEvent(
                userPet = userPet,
                petPointAction = petPointAction,
            ),
        )
    }
}
