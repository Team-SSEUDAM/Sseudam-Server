package com.sseudam.user.dto

import com.sseudam.user.UserProfile
import java.time.LocalDateTime

data class SendMessageUserProfileDto(
    val id: Long,
    val email: String,
    val nickname: String,
    val site: String,
    val createdAt: LocalDateTime,
) {
    companion object {
        fun from(userProfile: UserProfile) =
            SendMessageUserProfileDto(
                id = userProfile.id,
                email = userProfile.email,
                nickname = userProfile.nickname,
                site = userProfile.address.site,
                createdAt = userProfile.createdAt,
            )
    }
}
