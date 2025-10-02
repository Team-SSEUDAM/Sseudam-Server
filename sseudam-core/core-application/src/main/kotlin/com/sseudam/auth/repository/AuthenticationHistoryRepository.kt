package com.sseudam.auth.repository

import com.sseudam.auth.AuthenticationHistory
import com.sseudam.auth.command.AuthenticationHistoryCommand
import com.sseudam.auth.command.UpdateAuthenticationHistoryCommand

interface AuthenticationHistoryRepository {
    fun create(authenticationHistoryCommand: AuthenticationHistoryCommand): AuthenticationHistory

    fun findUserKeyWithDeviceWithRefreshToken(
        userKey: String,
        deviceId: String?,
        refreshToken: String,
    ): AuthenticationHistory?

    fun update(updateAuthenticationHistoryCommand: UpdateAuthenticationHistoryCommand): AuthenticationHistory?

    fun findUserKey(userKey: String): AuthenticationHistory?

    fun findUserId(userId: Long): AuthenticationHistory?

    fun removeToken(userKey: String): List<String>?

    fun remove(token: String): String
}
