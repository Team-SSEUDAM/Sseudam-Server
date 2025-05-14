package com.sseudam.auth

import com.sseudam.support.error.AuthenticationErrorException
import com.sseudam.support.error.AuthenticationErrorType
import org.springframework.stereotype.Component

@Component
class AuthenticationHistoryReader(
    private val authenticationHistoryRepository: AuthenticationHistoryRepository,
) {
    fun readByUserKeyWithDeviceWithRefreshToken(
        userKey: String,
        deviceId: String?,
        refreshToken: String,
    ): AuthenticationHistory =
        authenticationHistoryRepository.findUserKeyWithDeviceWithRefreshToken(userKey, deviceId, refreshToken)
            ?: throw AuthenticationErrorException(AuthenticationErrorType.NOT_FOUND_HISTORY)

    fun readByUserKey(userKey: String): AuthenticationHistory? = authenticationHistoryRepository.findUserKey(userKey)

    fun readByUserId(userId: Long): AuthenticationHistory? = authenticationHistoryRepository.findUserId(userId)
}
