package com.sseudam.pet.component

import com.sseudam.pet.UserPet
import com.sseudam.pet.repository.UserPetRepository
import org.springframework.stereotype.Component

@Component
class UserPetReader(
    private val userPetRepository: UserPetRepository,
) {
    fun readPetInfoByUser(userId: Long): UserPet.Info? = userPetRepository.findByUserId(userId)

    fun readAll(): List<UserPet.Info> = userPetRepository.findAll()
}
