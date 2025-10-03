package com.sseudam.storage.db.core.user

import com.sseudam.common.Address
import com.sseudam.support.cursor.OffsetPageRequest
import com.sseudam.support.error.ErrorException
import com.sseudam.support.error.ErrorType
import com.sseudam.support.page.Page
import com.sseudam.support.tx.Tx
import com.sseudam.user.SocialUser
import com.sseudam.user.User
import com.sseudam.user.UserCredentials
import com.sseudam.user.UserProfile
import com.sseudam.user.command.UserCommand
import com.sseudam.user.command.UserKeyCommand
import com.sseudam.user.repository.UserRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Repository

@Repository
class UserCoreRepository(
    private val userJpaRepository: UserJpaRepository,
    private val userCustomRepository: UserCustomRepository,
) : UserRepository {
    override fun create(
        userCommand: UserCommand,
        userKeyCommand: UserKeyCommand,
    ): User =
        Tx.writeable {
            userJpaRepository.save(UserEntity(userCommand, userKeyCommand)).toUser()
        }

    override fun readUserById(id: Long): User? =
        Tx.readable {
            userJpaRepository.findByIdOrNull(id)?.toUser()
        }

    override fun readUser(
        loginId: String,
        password: String,
    ): User =
        Tx.readable {
            userJpaRepository.findByEmailAndDeletedAtIsNull(loginId)?.toUser()
                ?: throw ErrorException(ErrorType.NOT_FOUND_DATA)
        }

    override fun readUserCredentials(loginId: String): UserCredentials =
        Tx.readable {
            userJpaRepository.findByEmailAndDeletedAtIsNull(loginId)?.toUserCredentials()
                ?: throw ErrorException(ErrorType.NOT_FOUND_DATA)
        }

    override fun readByUserIdOrNull(id: Long): UserProfile? =
        Tx.readable {
            userJpaRepository.findByIdAndDeletedAtIsNull(id)?.toProfile()
        }

    override fun readAllByUserIds(userIds: List<Long>): List<UserProfile> =
        Tx.readable {
            userJpaRepository.findAllByIdIn(userIds).map { it.toProfile() }
        }

    override fun readByUserKey(userKey: String): UserProfile =
        Tx.readable {
            userJpaRepository.findByUserKeyAndDeletedAtIsNull(userKey)?.toProfile()
                ?: throw ErrorException(ErrorType.NOT_FOUND_DATA)
        }

    override fun findProfileByUserId(id: Long): UserProfile? =
        Tx.readable {
            userJpaRepository.findByIdAndDeletedAtIsNull(id)?.toProfile()
        }

    override fun readUserByEmail(email: String): SocialUser? =
        Tx.readable {
            userJpaRepository.findByEmailAndDeletedAtIsNull(email)?.toSocialUser()
        }

    override fun findAll(): List<UserProfile> =
        Tx.readable {
            userJpaRepository.findAllByDeletedAtIsNull().map { it.toProfile() }
        }

    override fun existsByEmail(email: String): Boolean =
        Tx.readable {
            userJpaRepository.existsByEmailAndDeletedAtIsNull(email)
        }

    override fun readAllBy(offsetPageRequest: OffsetPageRequest): Page<UserProfile> =
        Tx.readable {
            userCustomRepository.findAllBy(offsetPageRequest)
        }

    override fun existsByNickname(nickname: String): Boolean =
        Tx.readable {
            userJpaRepository.existsByNicknameAndDeletedAtIsNull(nickname)
        }

    override fun updateNickname(
        userKey: String,
        nickname: String,
    ): UserProfile =
        Tx.writeable {
            val user = userJpaRepository.findByUserKeyAndDeletedAtIsNull(userKey) ?: throw ErrorException(ErrorType.NOT_FOUND_DATA)
            user.updateNickname(nickname)
            return@writeable user.toProfile()
        }

    override fun updateName(
        userKey: String,
        name: String,
    ): UserProfile =
        Tx.writeable {
            val user = userJpaRepository.findByUserKeyAndDeletedAtIsNull(userKey) ?: throw ErrorException(ErrorType.NOT_FOUND_DATA)
            user.updateName(name)
            user.updateNickname(name)
            return@writeable user.toProfile()
        }

    override fun updateAddress(
        userKey: String,
        address: Address,
    ): UserProfile =
        Tx.writeable {
            val user = userJpaRepository.findByUserKeyAndDeletedAtIsNull(userKey) ?: throw ErrorException(ErrorType.NOT_FOUND_DATA)
            user.updateAddress(address)
            user.toProfile()
        }

    override fun updateEmail(
        userKey: String,
        email: String,
    ): UserProfile =
        Tx.writeable {
            val user = userJpaRepository.findByUserKeyAndDeletedAtIsNull(userKey) ?: throw ErrorException(ErrorType.NOT_FOUND_DATA)
            user.updateEmail(email)
            user.toProfile()
        }

    override fun delete(userKey: String) =
        Tx.writeable {
            val user = userJpaRepository.findByUserKeyAndDeletedAtIsNull(userKey) ?: throw ErrorException(ErrorType.NOT_FOUND_DATA)
            user.softDelete()
        }
}
