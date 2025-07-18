package com.sseudam.pet

import org.springframework.stereotype.Component

@Component
class UserPetDeleter(
    private val userPetRepository: UserPetRepository,
) {
    fun deleteAllByUserIds(userIds: List<Long>) {
        userPetRepository.deleteAllByUserIds(userIds)
    }
}
