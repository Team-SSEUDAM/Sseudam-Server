package com.sseudam.user

import com.sseudam.common.Address
import com.sseudam.pet.Pet
import com.sseudam.pet.PetReader
import com.sseudam.pet.UserPetAppender
import com.sseudam.support.cursor.OffsetPageRequest
import com.sseudam.support.error.ErrorException
import com.sseudam.support.error.ErrorType
import com.sseudam.support.page.Page
import com.sseudam.support.tx.TxAdvice
import org.springframework.stereotype.Service
import java.time.LocalDate

@Service
class UserService(
    private val userAppender: UserAppender,
    private val userReader: UserReader,
    private val userUpdater: UserUpdater,
    private val userDeleter: UserDeleter,
    private val userValidator: UserValidator,
    private val userPetAppender: UserPetAppender,
    private val petReader: PetReader,
    private val txAdvice: TxAdvice,
) {
    fun create(newUser: NewUser): User =
        txAdvice.write {
            userValidator.verifyEmail(newUser.email)
            val (currentYear, currentMonth) = LocalDate.now().let { it.year to it.month }
            val pets = petReader.readAllLatestSeasonPets(currentYear, currentMonth)
            val level1Pet =
                pets.find { it.levelType == Pet.LevelType.LEVEL_1 }
                    ?: throw ErrorException(ErrorType.INVALID_PET_LEVEL_TYPE)
            val createUser = userAppender.create(newUser)
            userPetAppender.append(createUser.id, level1Pet)
            return@write createUser
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
        updateNickname: UpdateNickname,
    ): UserProfile {
        userValidator.verifyNickname(updateNickname.nickname)
        return userUpdater.updateNickname(userKey, updateNickname)
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

    fun deleteUser(newUserWithdrawal: NewUserWithdrawal) {
        userDeleter.deleteUser(newUserWithdrawal.user.key)
    }
}
