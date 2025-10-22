package com.sseudam.user.component

import com.sseudam.support.error.ErrorException
import com.sseudam.support.error.ErrorType
import com.sseudam.support.page.OffsetPageRequest
import com.sseudam.support.page.Page
import com.sseudam.user.SocialUser
import com.sseudam.user.User
import com.sseudam.user.UserCredentials
import com.sseudam.user.UserProfile
import com.sseudam.user.repository.UserRepository
import org.springframework.stereotype.Component

@Component
class UserReader(
    private val userRepository: UserRepository,
) {
    fun readUserProfile(userId: Long): UserProfile? = userRepository.findProfileByUserId(userId)

    fun readUserProfileOrNull(userId: Long): UserProfile? = userRepository.readByUserIdOrNull(userId)

    fun readUserProfile(userKey: String): UserProfile = userRepository.readByUserKey(userKey)

    fun readUser(userId: Long): User {
        val user = userRepository.readUserById(userId) ?: throw ErrorException(ErrorType.NOT_FOUND_USER)
        return user
    }

    fun readUser(
        loginId: String,
        password: String,
    ): User = userRepository.readUser(loginId, password)

    fun readUserCredentials(loginId: String): UserCredentials = userRepository.readUserCredentials(loginId)

    fun readAllByUserIds(userIds: List<Long>): List<UserProfile> = userRepository.readAllByUserIds(userIds)

    fun readUserByEmail(email: String): SocialUser? = userRepository.readUserByEmail(email)

    fun readAllBy(offsetPageRequest: OffsetPageRequest): Page<UserProfile> = userRepository.readAllBy(offsetPageRequest)

    fun readAll(): List<UserProfile> = userRepository.findAll()
}
