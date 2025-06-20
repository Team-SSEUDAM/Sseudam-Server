package com.sseudam.pet

import org.springframework.stereotype.Component

@Component
class UserPetAppender(
    private val userPetRepository: UserPetRepository,
) {
    fun append(
        userId: Long,
        pet: Pet.Info,
    ): UserPet.Info =
        userPetRepository.save(
            UserPet.Create(
                userId = userId,
                petId = pet.id,
                nickname = "냥이",
                point = 0L,
            ),
        )
}
