package com.sseudam.pet.component

import com.sseudam.pet.Pet
import com.sseudam.pet.repository.PetRepository
import org.springframework.stereotype.Component

@Component
class PetAppender(
    private val petRepository: PetRepository,
) {
    fun appendSeasonPet(create: Pet.Create): Pet.Info = petRepository.save(create)
}
