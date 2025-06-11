package com.sseudam.admin.presentation.response

import com.sseudam.admin.domain.AdminToken

data class AdminTokenResponse(
    val accessToken: String,
    val refreshToken: String,
) {
    companion object {
        fun of(adminToken: AdminToken): AdminTokenResponse = AdminTokenResponse(adminToken.accessToken, adminToken.refreshToken)
    }
}
