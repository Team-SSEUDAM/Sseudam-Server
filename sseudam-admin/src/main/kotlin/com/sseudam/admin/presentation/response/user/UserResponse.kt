package com.sseudam.admin.presentation.response.user

import com.sseudam.user.UserProfile
import java.time.LocalDateTime

data class UserResponse(
    val userId: Long,
    val email: String,
    val nickname: String,
    val createdAt: LocalDateTime,
) {
    companion object {
        fun of(userProfile: UserProfile): UserResponse =
            UserResponse(
                userProfile.id,
                userProfile.email,
                userProfile.nickname,
                userProfile.createdAt,
            )
    }
}
