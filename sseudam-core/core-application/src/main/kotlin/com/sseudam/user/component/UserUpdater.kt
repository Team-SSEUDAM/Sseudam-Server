package com.sseudam.user.component

import com.sseudam.common.Address
import com.sseudam.user.UserProfile
import com.sseudam.user.command.UpdateNicknameCommand
import com.sseudam.user.repository.UserRepository
import org.springframework.stereotype.Component

@Component
class UserUpdater(
    private val userRepository: UserRepository,
) {
    fun updateNickname(
        userKey: String,
        updateNicknameCommand: UpdateNicknameCommand,
    ): UserProfile = userRepository.updateNickname(userKey, updateNicknameCommand.nickname)

    fun updateName(
        userKey: String,
        name: String,
    ): UserProfile = userRepository.updateName(userKey, name)

    fun updateEmail(
        userKey: String,
        email: String,
    ): UserProfile = userRepository.updateEmail(userKey, email)

    fun updateAddress(
        userKey: String,
        address: Address,
    ): UserProfile = userRepository.updateAddress(userKey, address)
}
