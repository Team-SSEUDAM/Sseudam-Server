package com.sseudam.user.component

import com.sseudam.user.repository.UserRepository
import org.springframework.stereotype.Component

@Component
class UserDeleter(
    private val userRepository: UserRepository,
) {
    fun deleteUser(userKey: String) {
        userRepository.delete(userKey)
    }
}
