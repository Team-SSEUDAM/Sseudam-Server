package com.sseudam.admin.presentation.response.user

import com.sseudam.admin.domain.AdminUserProfile
import com.sseudam.trashspot.TrashSpot
import java.time.LocalDateTime

data class AdminUserResponse(
    val userId: Long,
    val email: String,
    val nickname: String,
    val createdAt: LocalDateTime,
    val visitedSpot: MutableList<TrashSpot.Info>,
) {
    companion object {
        fun of(user: AdminUserProfile): AdminUserResponse =
            AdminUserResponse(
                userId = user.id,
                email = user.email,
                nickname = user.nickname,
                createdAt = user.createdAt,
                visitedSpot = user.visitedSpot,
            )
    }
}
