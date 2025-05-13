package com.sseudam.user

interface UserRepository {
    // Create
    fun create(
        newUser: NewUser,
        newUserKey: NewUserKey,
    ): User

    // Read
    suspend fun readByUserId(id: Long): UserProfile

    suspend fun readByUserKey(userKey: String): UserProfile

    fun readUserById(id: Long): User?

    fun readUser(
        loginId: String,
        password: String,
    ): User

    suspend fun readByUserIdOrNull(id: Long): UserProfile?

    suspend fun readAllByUserIds(userIds: List<Long>): List<UserProfile>

    suspend fun existsByEmail(email: String): Boolean

    suspend fun existsByNickname(nickname: String): Boolean

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

    suspend fun updateEmail(
        userKey: String,
        email: String,
    ): UserProfile

    // Delete
    fun delete(userKey: String)
}
