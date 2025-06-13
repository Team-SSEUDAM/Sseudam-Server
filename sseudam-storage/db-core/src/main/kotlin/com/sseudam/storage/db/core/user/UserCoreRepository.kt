package com.sseudam.storage.db.core.user

import com.sseudam.storage.db.core.support.findByIdAndDeletedAtIsNullOrElseThrow
import com.sseudam.support.cursor.OffsetPageRequest
import com.sseudam.support.error.ErrorException
import com.sseudam.support.error.ErrorType
import com.sseudam.support.tx.TxAdvice
import com.sseudam.user.NewUser
import com.sseudam.user.NewUserKey
import com.sseudam.user.SocialUser
import com.sseudam.user.User
import com.sseudam.user.UserCredentials
import com.sseudam.user.UserProfile
import com.sseudam.user.UserRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Repository

@Repository
class UserCoreRepository(
    private val userJpaRepository: UserJpaRepository,
    private val userCustomRepository: UserCustomRepository,
    private val txAdvice: TxAdvice,
) : UserRepository {
    override fun create(
        newUser: NewUser,
        newUserKey: NewUserKey,
    ): User =
        txAdvice.write {
            userJpaRepository.save(UserEntity(newUser, newUserKey)).toUser()
        }

    override fun readUserById(id: Long): User? =
        txAdvice.readOnly {
            userJpaRepository.findByIdOrNull(id)?.toUser()
        }

    override fun readUser(
        loginId: String,
        password: String,
    ): User =
        txAdvice.readOnly {
            userJpaRepository.findByEmailAndPasswordAndDeletedAtIsNull(loginId, password)?.toUser()
                ?: throw ErrorException(ErrorType.NOT_FOUND_DATA)
        }

    override fun readUserCredentials(loginId: String): UserCredentials =
        txAdvice.readOnly {
            userJpaRepository.findByEmailAndDeletedAtIsNull(loginId)?.toUserCredentials()
                ?: throw ErrorException(ErrorType.NOT_FOUND_DATA)
        }

    override fun readByUserIdOrNull(id: Long): UserProfile? =
        txAdvice.readOnly {
            userJpaRepository.findByIdAndDeletedAtIsNull(id)?.toProfile()
        }

    override fun readAllByUserIds(userIds: List<Long>): List<UserProfile> =
        txAdvice.readOnly {
            userJpaRepository.findAllByIdIn(userIds).map { it.toProfile() }
        }

    override fun readByUserKey(userKey: String): UserProfile =
        txAdvice.readOnly {
            userJpaRepository.findByUserKeyAndDeletedAtIsNull(userKey)?.toProfile()
                ?: throw ErrorException(ErrorType.NOT_FOUND_DATA)
        }

    override fun readByUserId(id: Long): UserProfile =
        txAdvice.readOnly {
            userJpaRepository.findByIdAndDeletedAtIsNullOrElseThrow(id).toProfile()
        }

    override fun readUserByEmail(email: String): SocialUser? =
        txAdvice.readOnly {
            userJpaRepository.findByEmailAndDeletedAtIsNull(email)?.toSocialUser()
        }

    override fun existsByEmail(email: String): Boolean =
        txAdvice.readOnly {
            userJpaRepository.existsByEmailAndDeletedAtIsNull(email)
        }

    override fun readAllBy(offsetPageRequest: OffsetPageRequest): List<UserProfile> =
        txAdvice.readOnly {
            userCustomRepository.findAllBy(offsetPageRequest)
        }

    override fun existsByNickname(nickname: String): Boolean =
        txAdvice.readOnly {
            userJpaRepository.existsByNicknameAndDeletedAtIsNull(nickname)
        }

    override fun updateNickname(
        userKey: String,
        nickname: String,
    ): UserProfile =
        txAdvice.write {
            val user = userJpaRepository.findByUserKeyAndDeletedAtIsNull(userKey) ?: throw ErrorException(ErrorType.NOT_FOUND_DATA)
            user.updateNickname(nickname)
            return@write user.toProfile()
        }

    override fun updateName(
        userKey: String,
        name: String,
    ): UserProfile =
        txAdvice.write {
            val user = userJpaRepository.findByUserKeyAndDeletedAtIsNull(userKey) ?: throw ErrorException(ErrorType.NOT_FOUND_DATA)
            user.updateName(name)
            user.updateNickname(name)
            return@write user.toProfile()
        }

    override fun updateEmail(
        userKey: String,
        email: String,
    ): UserProfile =
        txAdvice.write {
            val user = userJpaRepository.findByUserKeyAndDeletedAtIsNull(userKey) ?: throw ErrorException(ErrorType.NOT_FOUND_DATA)
            user.updateEmail(email)
            user.toProfile()
        }

    override fun delete(userKey: String) =
        txAdvice.write {
            val user = userJpaRepository.findByUserKeyAndDeletedAtIsNull(userKey) ?: throw ErrorException(ErrorType.NOT_FOUND_DATA)
            user.softDelete()
        }
}
