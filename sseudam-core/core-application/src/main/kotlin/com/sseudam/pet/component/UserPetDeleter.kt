package com.sseudam.pet.component

import com.sseudam.pet.repository.UserPetRepository
import org.springframework.stereotype.Component

@Component
class UserPetDeleter(
    private val userPetRepository: UserPetRepository,
) {
    fun deleteAllByUserIds(userIds: List<Long>) {
        userPetRepository.deleteAllByUserIds(userIds)
    }

    fun deleteByUser(userId: Long) {
        userPetRepository.deleteByUserId(userId)
    }
}
