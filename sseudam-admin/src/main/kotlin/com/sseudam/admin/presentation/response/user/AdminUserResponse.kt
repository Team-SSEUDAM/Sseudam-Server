package com.sseudam.admin.presentation.response.user

import com.sseudam.admin.domain.AdminUserProfile
import com.sseudam.visit.SpotVisited
import java.time.LocalDateTime

data class AdminUserResponse(
    val userId: Long,
    val email: String,
    val nickname: String,
    val createdAt: LocalDateTime,
    val visited: MutableList<SpotVisited.Info>,
) {
    companion object {
        fun of(user: AdminUserProfile): AdminUserResponse =
            AdminUserResponse(
                userId = user.id,
                email = user.email,
                nickname = user.nickname,
                createdAt = user.createdAt,
                visited = user.visited,
            )
    }
}
