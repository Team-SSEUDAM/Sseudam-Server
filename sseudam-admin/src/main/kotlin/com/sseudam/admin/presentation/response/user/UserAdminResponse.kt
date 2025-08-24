package com.sseudam.admin.presentation.response.user

import com.sseudam.user.UserProfile
import java.time.LocalDateTime

data class UserAdminResponse(
    val userId: Long,
    val email: String,
    val nickname: String,
    val createdAt: LocalDateTime,
) {
    companion object {
        fun of(userProfile: UserProfile): UserAdminResponse =
            UserAdminResponse(
                userProfile.id,
                userProfile.email,
                userProfile.nickname,
                userProfile.createdAt,
            )
    }
}
