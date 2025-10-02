package com.sseudam.auth.component

import com.sseudam.auth.AuthenticationHistory
import com.sseudam.auth.command.UpdateAuthenticationHistoryCommand
import com.sseudam.auth.repository.AuthenticationHistoryRepository
import com.sseudam.support.error.AuthenticationErrorException
import com.sseudam.support.error.AuthenticationErrorType
import org.springframework.stereotype.Component

@Component
class AuthenticationHistoryUpdater(
    private val authenticationHistoryRepository: AuthenticationHistoryRepository,
) {
    fun update(updateAuthenticationHistoryCommand: UpdateAuthenticationHistoryCommand): AuthenticationHistory =
        authenticationHistoryRepository.update(updateAuthenticationHistoryCommand)
            ?: throw AuthenticationErrorException(AuthenticationErrorType.NOT_FOUND_HISTORY)

    fun removeToken(userKey: String): List<String> =
        authenticationHistoryRepository.removeToken(userKey)
            ?: throw AuthenticationErrorException(AuthenticationErrorType.NOT_FOUND_HISTORY)

    fun remove(token: String): String = authenticationHistoryRepository.remove(token)
}
