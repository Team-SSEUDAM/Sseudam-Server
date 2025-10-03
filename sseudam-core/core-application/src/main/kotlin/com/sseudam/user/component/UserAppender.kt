package com.sseudam.user.component

import com.sseudam.user.User
import com.sseudam.user.command.UserCommand
import com.sseudam.user.repository.UserRepository
import org.springframework.stereotype.Component

@Component
class UserAppender(
    private val userRepository: UserRepository,
    private val userKeyGenerator: UserKeyGenerator,
) {
    fun create(userCommand: UserCommand): User {
        val newUserKey = userKeyGenerator.generate()
        return userRepository.create(
            userCommand = userCommand,
            userKeyCommand = newUserKey,
        )
    }
}
