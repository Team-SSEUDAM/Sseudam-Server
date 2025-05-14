package com.sseudam.storage.db.core.auth

import com.sseudam.auth.AuthenticationHistory
import com.sseudam.auth.NewAuthenticationHistory
import com.sseudam.auth.token.Token
import com.sseudam.auth.token.TokenStatus
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Table

@Entity
@Table(name = "t_authentication_history")
class AuthenticationHistoryEntity(
    val userId: Long,
    val userKey: String,
    val deviceId: String?,
    @Column(columnDefinition = "TEXT")
    var accessToken: String,
    @Column(columnDefinition = "TEXT")
    var refreshToken: String,
) : AuthenticationBaseEntity() {
    constructor(
        newAuthenticationHistory: NewAuthenticationHistory,
    ) : this(
        userId = newAuthenticationHistory.userId,
        userKey = newAuthenticationHistory.userKey,
        deviceId = newAuthenticationHistory.deviceId,
        accessToken = newAuthenticationHistory.newToken.token.accessToken,
        refreshToken = newAuthenticationHistory.newToken.token.refreshToken,
    )

    fun toAuthenticationHistory(): AuthenticationHistory =
        AuthenticationHistory(
            authenticationId = id!!,
            userKey = userKey,
            deviceId = deviceId,
            token =
                Token(
                    accessToken = accessToken,
                    refreshToken = refreshToken,
                ),
            status = entityStatus.toTokenStatus(),
            loggedInAt = updatedAt ?: createdAt,
        )

    fun updateRefreshToken(token: Token): AuthenticationHistory {
        this.accessToken = token.accessToken
        this.refreshToken = token.refreshToken
        return AuthenticationHistory(
            authenticationId = id!!,
            userKey = userKey,
            deviceId = deviceId,
            token =
                Token(
                    accessToken = token.accessToken,
                    refreshToken = token.refreshToken,
                ),
            status = entityStatus.toTokenStatus(),
            loggedInAt = updatedAt ?: createdAt,
        )
    }

    internal fun AuthenticationEntityStatus.toTokenStatus(): TokenStatus =
        when (this) {
            AuthenticationEntityStatus.ACTIVE -> TokenStatus.ACTIVE
            AuthenticationEntityStatus.DELETE -> TokenStatus.INACTIVE
        }
}
