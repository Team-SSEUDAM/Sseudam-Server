package com.sseudam.user

import com.sseudam.common.Address
import com.sseudam.support.cursor.OffsetPageRequest
import com.sseudam.support.page.Page
import com.sseudam.support.tx.Tx
import com.sseudam.user.command.UpdateNicknameCommand
import com.sseudam.user.command.UserCommand
import com.sseudam.user.command.UserWithdrawalCommand
import com.sseudam.user.component.UserAppender
import com.sseudam.user.component.UserDeleter
import com.sseudam.user.component.UserReader
import com.sseudam.user.component.UserUpdater
import com.sseudam.user.component.UserValidator
import com.sseudam.user.event.UserSignUpEvent
import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Service

@Service
class UserService(
    private val userAppender: UserAppender,
    private val userReader: UserReader,
    private val userUpdater: UserUpdater,
    private val userDeleter: UserDeleter,
    private val userValidator: UserValidator,
    private val applicationEventPublisher: ApplicationEventPublisher,
) {
    fun create(userCommand: UserCommand): User =
        Tx.writeable {
            userValidator.verifyEmail(userCommand.email)
            val createdUser = userAppender.create(userCommand)
            return@writeable createdUser
        }

    fun socialSignUp(
        socialUser: SocialUser,
        userCommand: UserCommand,
    ) = Tx.writeable {
        updateName(socialUser.key, userCommand.name)
        userCommand.address?.let { address ->
            updateAddress(socialUser.key, address)
        }
        applicationEventPublisher.publishEvent(UserSignUpEvent(userId = socialUser.id))
    }

    fun getProfile(userId: Long): UserProfile? = userReader.readUserProfile(userId)

    fun getSocialUserByEmail(email: String): SocialUser? = userReader.readUserByEmail(email)

    fun getUser(userId: Long): User = userReader.readUser(userId)

    fun getUser(
        loginId: String,
        password: String,
    ): User = userReader.readUser(loginId, password)

    fun getUserCredential(loginId: String): UserCredentials = userReader.readUserCredentials(loginId)

    fun findAllBy(userIds: List<Long>): List<UserProfile> = userReader.readAllByUserIds(userIds)

    fun checkEmail(email: String) {
        userValidator.verifyEmail(email)
    }

    fun findUserProfileBy(offsetPageRequest: OffsetPageRequest): Page<UserProfile> = userReader.readAllBy(offsetPageRequest)

    fun findAll(): List<UserProfile> = userReader.readAll()

    fun updateNickname(
        userKey: String,
        updateNicknameCommand: UpdateNicknameCommand,
    ): UserProfile {
        userValidator.verifyNickname(updateNicknameCommand.nickname)
        return userUpdater.updateNickname(userKey, updateNicknameCommand)
    }

    fun updateName(
        userKey: String,
        name: String,
    ): UserProfile {
        userValidator.verifyNickname(name)
        return userUpdater.updateName(userKey, name)
    }

    fun updateAddress(
        userKey: String,
        address: Address,
    ): UserProfile = userUpdater.updateAddress(userKey, address)

    fun validateNickname(nickname: String): Boolean {
        userValidator.verifyNickname(nickname)
        return true
    }

    fun updateEmail(
        userKey: String,
        email: String,
    ): UserProfile = userUpdater.updateEmail(userKey, email)

    fun deleteUser(userWithdrawalCommand: UserWithdrawalCommand) {
        userDeleter.deleteUser(userWithdrawalCommand.user.key)
    }
}
