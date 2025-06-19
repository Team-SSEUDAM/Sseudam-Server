package com.sseudam.storage.db.core.pet

import com.sseudam.pet.UserPet
import com.sseudam.pet.UserPetRepository
import org.springframework.stereotype.Repository

@Repository
class UserPetCoreRepository(
    private val userPetJpaRepository: UserPetJpaRepository,
) : UserPetRepository {
    override fun save(createUserPet: UserPet.Create): UserPet.Info =
        userPetJpaRepository
            .save(
                UserPetEntity(
                    createUserPet,
                ),
            ).toUserPetInfo()

    override fun findByUserId(userId: Long): UserPet.Info? = userPetJpaRepository.findByUserIdAndDeletedAtIsNull(userId)?.toUserPetInfo()
}
