package com.sseudam.pet

import org.springframework.stereotype.Service

@Service
class UserPetService(
    private val userPetAppender: UserPetAppender,
    private val userPetReader: UserPetReader,
) {
    fun findPetInfo(userId: Long): UserPet.Info = userPetReader.findPetInfo(userId) ?: userPetAppender.append(userId)
}
