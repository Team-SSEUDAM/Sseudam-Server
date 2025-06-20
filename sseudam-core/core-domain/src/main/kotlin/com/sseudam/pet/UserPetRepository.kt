package com.sseudam.pet

interface UserPetRepository {
    fun save(createUserPet: UserPet.Create): UserPet.Info

    fun findByUserId(userId: Long): UserPet.Info?

    fun updateNickname(
        userId: Long,
        nickname: String,
    ): UserPet.Info

    fun updatePetId(
        userPetId: Long,
        petId: Long,
    ): UserPet.Info

    fun updatePoint(
        userPetId: Long,
        action: PetPointAction,
    ): UserPet.Info
}
