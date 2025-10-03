package com.sseudam.auth.component

import com.sseudam.auth.command.AuthenticationHistoryCommand
import com.sseudam.auth.repository.AuthenticationHistoryRepository
import org.springframework.stereotype.Component

@Component
class AuthenticationHistoryWriter(
    private val authenticationHistoryRepository: AuthenticationHistoryRepository,
) {
    fun write(authenticationHistoryCommand: AuthenticationHistoryCommand) {
        authenticationHistoryRepository.create(authenticationHistoryCommand)
    }
}
