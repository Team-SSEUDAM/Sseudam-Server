package com.sseudam.admin.domain

import com.sseudam.contract.trashspot.TrashSpotDto
import com.sseudam.user.UserProfile
import java.time.LocalDateTime

data class AdminUserProfile(
    val id: Long,
    val key: String,
    val email: String,
    val name: String?,
    val nickname: String,
    val createdAt: LocalDateTime,
    val visitedSpot: List<TrashSpotDto>,
) {
    companion object {
        fun of(
            profile: UserProfile,
            visitedSpot: List<TrashSpotDto>,
        ): AdminUserProfile =
            AdminUserProfile(
                id = profile.id,
                key = profile.key,
                email = profile.email,
                name = profile.name,
                nickname = profile.nickname,
                createdAt = profile.createdAt,
                visitedSpot = visitedSpot,
            )
    }
}
