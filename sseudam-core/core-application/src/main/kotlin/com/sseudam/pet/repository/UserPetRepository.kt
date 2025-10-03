package com.sseudam.pet.repository

import com.sseudam.pet.PetPointAction
import com.sseudam.pet.UserPet

interface UserPetRepository {
    fun save(createUserPet: UserPet.Create): UserPet.Info

    fun saveAll(createUserPets: List<UserPet.Create>): List<UserPet.Info>

    fun findByUserId(userId: Long): UserPet.Info?

    fun findAll(): List<UserPet.Info>

    fun updateNickname(
        userId: Long,
        nickname: String,
    ): UserPet.Info

    fun updatePetId(
        userPetId: Long,
        petId: Long,
    ): UserPet.Info

    fun updatePointByAction(
        userPetId: Long,
        action: PetPointAction,
    ): UserPet.Info

    fun updatePoint(
        userPetId: Long,
        point: Long,
    ): UserPet.Info

    fun initPoint(petId: Long)

    fun deleteAllByUserIds(userIds: List<Long>)

    fun deleteByUserId(userId: Long)
}
