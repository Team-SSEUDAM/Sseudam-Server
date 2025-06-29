package com.sseudam.pet

import org.springframework.stereotype.Service

@Service
class UserPetService(
    private val userPetAppender: UserPetAppender,
    private val userPetReader: UserPetReader,
    private val userPetUpdater: UserPetUpdater,
) {
    fun findByUser(userId: Long): UserPet.Info? = userPetReader.readPetInfoByUser(userId)

    fun append(
        userId: Long,
        pet: Pet.Info,
    ): UserPet.Info = userPetAppender.append(userId, pet)

    fun updatePoint(
        userPet: UserPet.Info,
        action: PetPointAction,
    ): UserPet.Info =
        userPetUpdater.updatePoint(
            userPetId = userPet.id,
            action = action,
        )

    fun addPointUserPet(
        userId: Long,
        point: Long,
    ): UserPet.Info {
        val userPet = userPetReader.readPetInfoByUser(userId)
        return userPetUpdater.addPoint(
            userPet = userPet!!,
            point = point,
        )
    }

    fun updatePetName(
        userId: Long,
        nickname: String,
    ): UserPet.Info = userPetUpdater.updatePetName(userId, nickname)

    fun updatePetId(
        userPetId: Long,
        petId: Long,
    ): UserPet.Info = userPetUpdater.updatePetId(userPetId, petId)
}
