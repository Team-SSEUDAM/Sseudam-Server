package com.sseudam.admin.presentation.response.user

import com.sseudam.support.page.Page
import com.sseudam.user.UserProfile

data class UserPageResponse(
    val list: List<UserResponse>,
    val totalCount: Long,
) {
    companion object {
        fun of(page: Page<UserProfile>) =
            UserPageResponse(
                list = page.content.map { UserResponse.of(it) },
                totalCount = page.totalCount,
            )
    }
}
