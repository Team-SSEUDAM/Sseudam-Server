package com.sseudam.auth.token.repository

import com.sseudam.auth.GrantedAuthority
import com.sseudam.auth.Provider
import com.sseudam.auth.token.Token
import com.sseudam.user.SocialUser
import com.sseudam.user.User

interface TokenRepository {
    fun create(
        deviceId: String?,
        user: User,
    ): Token

    fun create(
        deviceId: String?,
        socialUser: SocialUser,
    ): Token

    fun create(
        adminId: Long,
        grantedAuthorities: List<GrantedAuthority>,
    ): Token

    fun renew(refreshToken: String): Token

    fun adminRefresh(refreshToken: String): Token

    fun remove(token: String): String

    fun removeByUserKey(userKey: String)

    fun findBy(accessToken: String): Provider?
}
