package com.sseudam.pet

import org.springframework.stereotype.Component

@Component
class UserPetReader(
    private val userPetRepository: UserPetRepository,
) {
    fun findPetInfo(userId: Long): UserPet.Info? = userPetRepository.findByUserId(userId)
}
