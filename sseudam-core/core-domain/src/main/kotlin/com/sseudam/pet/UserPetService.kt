package com.sseudam.pet

import org.springframework.stereotype.Service

@Service
class UserPetService(
    private val userPetAppender: UserPetAppender,
    private val userPetReader: UserPetReader,
    private val userPetUpdater: UserPetUpdater,
    private val userPetDeleter: UserPetDeleter,
) {
    fun findByUser(userId: Long): UserPet.Info? = userPetReader.readPetInfoByUser(userId)

    fun findAll(): List<UserPet.Info> = userPetReader.readAll()

    fun append(
        userId: Long,
        pet: Pet.Info,
    ): UserPet.Info = userPetAppender.append(userId, pet)

    fun updatePointByAction(
        userPet: UserPet.Info,
        action: PetPointAction,
    ): UserPet.Info =
        userPetUpdater.updatePointByAction(
            userPetId = userPet.id,
            action = action,
        )

    fun updatePetName(
        userId: Long,
        nickname: String,
    ): UserPet.Info = userPetUpdater.updatePetName(userId, nickname)

    fun updatePetId(
        userPetId: Long,
        petId: Long,
    ): UserPet.Info = userPetUpdater.updatePetId(userPetId, petId)

    fun initPointForAllUsers(
        userPets: List<UserPet.Info>,
        petId: Long,
    ) {
        val userIds = userPets.map { it.userId }
        userPetDeleter.deleteAllByUserIds(userIds)
        userPetAppender.appendAll(userPets, petId)
    }

    fun deleteByUser(userId: Long) {
        userPetDeleter.deleteByUser(userId)
    }
}
