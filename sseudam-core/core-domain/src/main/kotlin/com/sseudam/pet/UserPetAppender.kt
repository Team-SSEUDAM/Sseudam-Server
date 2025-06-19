package com.sseudam.pet

import org.springframework.stereotype.Component

@Component
class UserPetAppender(
    private val userPetRepository: UserPetRepository,
) {
    fun append(userId: Long): UserPet.Info =
        userPetRepository.save(
            UserPet.Create(
                userId = userId,
                petId = 1L,
                name = Pet.LevelType.LEVEL_1.description,
                point = 0L,
            ),
        )
}
