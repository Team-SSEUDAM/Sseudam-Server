package com.sseudam.storage.db.core.auth

import com.sseudam.auth.AuthenticationHistory
import com.sseudam.auth.command.AuthenticationHistoryCommand
import com.sseudam.auth.command.UpdateAuthenticationHistoryCommand
import com.sseudam.auth.repository.AuthenticationHistoryRepository
import com.sseudam.support.error.AuthenticationErrorException
import com.sseudam.support.error.AuthenticationErrorType
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

@Repository
class AuthenticationHistoryCoreRepository(
    private val repository: AuthenticationHistoryJpaRepository,
) : AuthenticationHistoryRepository {
    @Transactional
    override fun create(authenticationHistoryCommand: AuthenticationHistoryCommand): AuthenticationHistory {
        val saveHistory = repository.save(AuthenticationHistoryEntity(authenticationHistoryCommand))
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
    override fun update(updateAuthenticationHistoryCommand: UpdateAuthenticationHistoryCommand): AuthenticationHistory? {
        val histories =
            repository.findAllByUserKeyAndDeviceId(
                userKey = updateAuthenticationHistoryCommand.userKey,
                deviceId = updateAuthenticationHistoryCommand.deviceId,
            )
        return histories
            .find {
                it.refreshToken == updateAuthenticationHistoryCommand.refreshToken
            }?.updateRefreshToken(updateAuthenticationHistoryCommand.tokenGenerateCommand.token)
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
