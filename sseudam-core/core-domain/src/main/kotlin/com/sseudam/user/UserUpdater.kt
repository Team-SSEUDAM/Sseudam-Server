package com.sseudam.user

import com.sseudam.common.Address
import org.springframework.stereotype.Component

@Component
class UserUpdater(
    private val userRepository: UserRepository,
) {
    fun updateNickname(
        userKey: String,
        updateNickname: UpdateNickname,
    ): UserProfile = userRepository.updateNickname(userKey, updateNickname.nickname)

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
