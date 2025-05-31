package com.sseudam.user

import com.sseudam.support.error.ErrorException
import com.sseudam.support.error.ErrorType
import org.springframework.stereotype.Component

@Component
class UserReader(
    private val userRepository: UserRepository,
) {
    fun readUserProfile(userId: Long): UserProfile = userRepository.readByUserId(userId)

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

    fun readAllByUserIds(userIds: List<Long>): List<UserProfile> = userRepository.readAllByUserIds(userIds)

    fun readUserByEmail(email: String): SocialUser? = userRepository.readUserByEmail(email)
}
