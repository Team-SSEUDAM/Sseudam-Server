package com.sseudam.storage.db.core.pet

import com.sseudam.pet.PetPointAction
import com.sseudam.pet.UserPet
import com.sseudam.pet.UserPetRepository
import com.sseudam.storage.db.core.support.findByIdOrElseThrow
import com.sseudam.support.error.ErrorException
import com.sseudam.support.error.ErrorType
import com.sseudam.support.tx.TxAdvice
import org.springframework.stereotype.Repository

@Repository
class UserPetCoreRepository(
    private val userPetJpaRepository: UserPetJpaRepository,
    private val txAdvice: TxAdvice,
) : UserPetRepository {
    override fun save(createUserPet: UserPet.Create): UserPet.Info =
        txAdvice.write {
            userPetJpaRepository
                .save(
                    UserPetEntity(
                        createUserPet,
                    ),
                ).toUserPetInfo()
        }

    override fun findByUserId(userId: Long): UserPet.Info? =
        txAdvice.readOnly {
            userPetJpaRepository.findByUserIdAndDeletedAtIsNull(userId)?.toUserPetInfo()
        }

    override fun updateNickname(
        userId: Long,
        nickname: String,
    ): UserPet.Info =
        txAdvice.write {
            val userPet =
                userPetJpaRepository
                    .findByUserIdAndDeletedAtIsNull(userId) ?: throw ErrorException(ErrorType.NOT_FOUND_DATA)
            userPet.updateNickname(nickname).toUserPetInfo()
        }

    override fun updatePetId(
        userPetId: Long,
        petId: Long,
    ): UserPet.Info =
        txAdvice.write {
            val userPet =
                userPetJpaRepository
                    .findByIdOrElseThrow(userPetId)
            userPet.updatePetId(petId).toUserPetInfo()
        }

    override fun updatePointByAction(
        userPetId: Long,
        action: PetPointAction,
    ): UserPet.Info =
        txAdvice.write {
            val userPet =
                userPetJpaRepository
                    .findByIdOrElseThrow(userPetId)
            userPet.updatePoint(userPet.point + action.point).toUserPetInfo()
        }

    override fun updatePoint(
        userPetId: Long,
        point: Long,
    ): UserPet.Info =
        txAdvice.write {
            val userPet =
                userPetJpaRepository
                    .findByIdOrElseThrow(userPetId)
            userPet.updatePoint(point).toUserPetInfo()
        }
}
