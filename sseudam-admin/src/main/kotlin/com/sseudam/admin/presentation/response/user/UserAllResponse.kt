package com.sseudam.admin.presentation.response.user

data class UserAllResponse(
    val list: List<UserResponse>,
) {
    companion object {
        fun of(list: List<UserResponse>): UserAllResponse = UserAllResponse(list)
    }
}
