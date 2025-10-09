package com.sseudam.user.repository

import com.sseudam.common.Address
import com.sseudam.support.page.OffsetPageRequest
import com.sseudam.support.page.Page
import com.sseudam.user.SocialUser
import com.sseudam.user.User
import com.sseudam.user.UserCredentials
import com.sseudam.user.UserProfile
import com.sseudam.user.command.UserCommand
import com.sseudam.user.command.UserKeyCommand

interface UserRepository {
    // Create
    fun create(
        userCommand: UserCommand,
        userKeyCommand: UserKeyCommand,
    ): User

    // Read
    fun findProfileByUserId(id: Long): UserProfile?

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

    fun readAllBy(offsetPageRequest: OffsetPageRequest): Page<UserProfile>

    fun existsByNickname(nickname: String): Boolean

    fun readUserByEmail(email: String): SocialUser?

    fun findAll(): List<UserProfile>

    // Update
    fun updateNickname(
        userKey: String,
        nickname: String,
    ): UserProfile

    fun updateName(
        userKey: String,
        name: String,
    ): UserProfile

    fun updateAddress(
        userKey: String,
        address: Address,
    ): UserProfile

    fun updateEmail(
        userKey: String,
        email: String,
    ): UserProfile

    // Delete
    fun delete(userKey: String)
}
