package com.sseudam.storage.db.core.pet

import com.sseudam.pet.UserPet
import com.sseudam.pet.UserPetRepository
import com.sseudam.support.error.ErrorException
import com.sseudam.support.error.ErrorType
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

    override fun updateNickname(
        userId: Long,
        nickname: String,
    ): UserPet.Info {
        val userPet =
            userPetJpaRepository
                .findByUserIdAndDeletedAtIsNull(userId) ?: throw ErrorException(ErrorType.NOT_FOUND_DATA)
        return userPet.updateNickname(nickname).toUserPetInfo()
    }
}
