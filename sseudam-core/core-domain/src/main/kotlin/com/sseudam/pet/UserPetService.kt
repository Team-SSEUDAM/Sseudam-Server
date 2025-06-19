package com.sseudam.pet

import org.springframework.stereotype.Service

@Service
class UserPetService(
    private val userPetAppender: UserPetAppender,
    private val userPetReader: UserPetReader,
    private val userPetUpdater: UserPetUpdater,
) {
    fun findPetInfo(userId: Long): UserPet.Info = userPetReader.findPetInfo(userId) ?: userPetAppender.append(userId)

    fun updatePetName(
        userId: Long,
        nickname: String,
    ): UserPet.Info = userPetUpdater.updatePetName(userId, nickname)
}
