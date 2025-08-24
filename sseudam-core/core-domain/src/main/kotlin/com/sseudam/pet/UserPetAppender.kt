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

    fun appendAll(
        userPets: List<UserPet.Info>,
        petId: Long,
    ) {
        userPetRepository.saveAll(
            userPets.map {
                UserPet.Create(
                    userId = it.userId,
                    petId = petId,
                    nickname = it.nickname,
                    point = 0L,
                )
            },
        )
    }
}
