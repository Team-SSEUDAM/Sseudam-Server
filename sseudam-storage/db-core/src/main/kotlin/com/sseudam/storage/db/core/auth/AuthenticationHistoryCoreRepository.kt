package com.sseudam.storage.db.core.auth

import com.sseudam.auth.AuthenticationHistory
import com.sseudam.auth.AuthenticationHistoryRepository
import com.sseudam.auth.NewAuthenticationHistory
import com.sseudam.auth.UpdateAuthenticationHistory
import com.sseudam.support.error.AuthenticationErrorException
import com.sseudam.support.error.AuthenticationErrorType
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

@Repository
class AuthenticationHistoryCoreRepository(
    private val repository: AuthenticationHistoryJpaRepository,
) : AuthenticationHistoryRepository {
    @Transactional
    override fun create(newAuthenticationHistory: NewAuthenticationHistory): AuthenticationHistory {
        val saveHistory = repository.save(AuthenticationHistoryEntity(newAuthenticationHistory))
        return saveHistory.toAuthenticationHistory()
    }

    override fun findUserKeyWithDeviceWithRefreshToken(
        userKey: String,
        deviceId: String?,
        refreshToken: String,
    ): AuthenticationHistory? {
        val histories =
            repository.findAllByUserKeyAndDeviceId(
                userKey = userKey,
                deviceId = deviceId,
            )
        return histories.find { it.refreshToken == refreshToken }?.toAuthenticationHistory()
    }

    @Transactional
    override fun update(updateAuthenticationHistory: UpdateAuthenticationHistory): AuthenticationHistory? {
        val histories =
            repository.findAllByUserKeyAndDeviceId(
                userKey = updateAuthenticationHistory.userKey,
                deviceId = updateAuthenticationHistory.deviceId,
            )
        return histories
            .find {
                it.refreshToken == updateAuthenticationHistory.refreshToken
            }?.updateRefreshToken(updateAuthenticationHistory.newToken.token)
    }

    @Transactional
    override fun removeToken(userKey: String): List<String>? =
        repository.findAllByUserKeyAndEntityStatus(userKey, AuthenticationEntityStatus.ACTIVE)?.map {
            it.delete()
            it.accessToken
        }

    override fun findUserKey(userKey: String): AuthenticationHistory? {
        val histories = repository.findAllByUserKeyAndEntityStatus(userKey, AuthenticationEntityStatus.ACTIVE)

        if (histories.isNullOrEmpty()) {
            return null
        }

        return histories.last().toAuthenticationHistory()
    }

    override fun findUserId(userId: Long): AuthenticationHistory? {
        val histories = repository.findAllByUserIdAndEntityStatus(userId, AuthenticationEntityStatus.ACTIVE)

        if (histories.isNullOrEmpty()) {
            return null
        }

        return histories.last().toAuthenticationHistory()
    }

    @Transactional
    override fun remove(token: String): String {
        val authenticationHistory =
            repository.findByAccessToken(token)
                ?: throw AuthenticationErrorException(AuthenticationErrorType.NOT_FOUND_HISTORY)
        authenticationHistory.delete()
        return authenticationHistory.refreshToken
    }
}
