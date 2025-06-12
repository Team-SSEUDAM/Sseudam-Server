package com.sseudam.user

import com.sseudam.support.cursor.OffsetPageRequest

interface UserRepository {
    // Create
    fun create(
        newUser: NewUser,
        newUserKey: NewUserKey,
    ): User

    // Read
    fun readByUserId(id: Long): UserProfile

    fun readByUserKey(userKey: String): UserProfile

    fun readUserById(id: Long): User?

    fun readUser(
        loginId: String,
        password: String,
    ): User

    fun readUserCredentials(loginId: String): UserCredentials

    fun readByUserIdOrNull(id: Long): UserProfile?

    fun readAllByUserIds(userIds: List<Long>): List<UserProfile>

    fun existsByEmail(email: String): Boolean

    fun readAllBy(offsetPageRequest: OffsetPageRequest): List<UserProfile>

    fun existsByNickname(nickname: String): Boolean

    fun readUserByEmail(email: String): SocialUser?

    // Update
    fun updateNickname(
        userKey: String,
        nickname: String,
    ): UserProfile

    fun updateName(
        userKey: String,
        name: String,
    ): UserProfile

    fun updateEmail(
        userKey: String,
        email: String,
    ): UserProfile

    // Delete
    fun delete(userKey: String)
}
