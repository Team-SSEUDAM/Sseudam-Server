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
                nickname = Pet.LevelType.LEVEL_1.defaultName,
                point = 0L,
            ),
        )
}
