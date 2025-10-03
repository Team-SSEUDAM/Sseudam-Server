package com.sseudam.auth.repository

import com.sseudam.auth.Token
import com.sseudam.auth.dto.GrantedAuthority
import com.sseudam.auth.dto.Provider
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

    fun adminTokenRemove(accessToken: String)

    fun remove(token: String): String

    fun removeByUserKey(userKey: String)

    fun findBy(accessToken: String): Provider?
}
