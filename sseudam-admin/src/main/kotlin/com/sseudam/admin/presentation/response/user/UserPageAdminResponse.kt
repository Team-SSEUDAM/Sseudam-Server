package com.sseudam.admin.presentation.response.user

import com.sseudam.support.page.Page
import com.sseudam.user.UserProfile

data class UserPageAdminResponse(
    val list: List<UserAdminResponse>,
    val totalCount: Long,
) {
    companion object {
        fun of(page: Page<UserProfile>) =
            UserPageAdminResponse(
                list = page.content.map { UserAdminResponse.of(it) },
                totalCount = page.totalCount,
            )
    }
}
