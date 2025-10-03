package com.sseudam.storage.db.core.pet

import com.sseudam.pet.PetPointAction
import com.sseudam.pet.UserPet
import com.sseudam.pet.repository.UserPetRepository
import com.sseudam.storage.db.core.support.findByIdAndDeletedAtIsNullOrElseThrow
import com.sseudam.support.error.ErrorException
import com.sseudam.support.error.ErrorType
import com.sseudam.support.tx.Tx
import org.springframework.stereotype.Repository

@Repository
class UserPetCoreRepository(
    private val userPetCustomRepository: UserPetCustomRepository,
    private val userPetJpaRepository: UserPetJpaRepository,
) : UserPetRepository {
    override fun save(createUserPet: UserPet.Create): UserPet.Info =
        Tx.writeable {
            userPetJpaRepository
                .save(
                    UserPetEntity(
                        createUserPet,
                    ),
                ).toUserPetInfo()
        }

    override fun saveAll(createUserPets: List<UserPet.Create>): List<UserPet.Info> =
        Tx.writeable {
            userPetJpaRepository
                .saveAll(
                    createUserPets.map { UserPetEntity(it) },
                ).map { it.toUserPetInfo() }
        }

    override fun findByUserId(userId: Long): UserPet.Info? =
        Tx.readable {
            userPetJpaRepository.findByUserIdAndDeletedAtIsNull(userId)?.toUserPetInfo()
        }

    override fun findAll(): List<UserPet.Info> =
        Tx.readable {
            userPetJpaRepository
                .findAllByDeletedAtIsNull()
                .map { it.toUserPetInfo() }
        }

    override fun updateNickname(
        userId: Long,
        nickname: String,
    ): UserPet.Info =
        Tx.writeable {
            val userPet =
                userPetJpaRepository
                    .findByUserIdAndDeletedAtIsNull(userId) ?: throw ErrorException(ErrorType.NOT_FOUND_DATA)
            userPet.updateNickname(nickname).toUserPetInfo()
        }

    override fun updatePetId(
        userPetId: Long,
        petId: Long,
    ): UserPet.Info =
        Tx.writeable {
            val userPet =
                userPetJpaRepository
                    .findByIdAndDeletedAtIsNullOrElseThrow(userPetId)
            userPet.updatePetId(petId).toUserPetInfo()
        }

    override fun updatePointByAction(
        userPetId: Long,
        action: PetPointAction,
    ): UserPet.Info =
        Tx.writeable {
            val userPet =
                userPetJpaRepository
                    .findByIdAndDeletedAtIsNullOrElseThrow(userPetId)
            userPet.updatePoint(userPet.point + action.point).toUserPetInfo()
        }

    override fun updatePoint(
        userPetId: Long,
        point: Long,
    ): UserPet.Info =
        Tx.writeable {
            val userPet =
                userPetJpaRepository
                    .findByIdAndDeletedAtIsNullOrElseThrow(userPetId)
            userPet.updatePoint(point).toUserPetInfo()
        }

    override fun initPoint(petId: Long) {
        userPetCustomRepository.resetSeasonUserPetPoint(petId)
    }

    override fun deleteAllByUserIds(userIds: List<Long>) {
        Tx.writeable {
            userPetCustomRepository.softDeleteAllByUserIds(userIds)
        }
    }

    override fun deleteByUserId(userId: Long) =
        Tx.writeable {
            val userPet =
                userPetJpaRepository.findByUserIdAndDeletedAtIsNull(userId)
            userPet?.softDelete()
            return@writeable
        }
}
