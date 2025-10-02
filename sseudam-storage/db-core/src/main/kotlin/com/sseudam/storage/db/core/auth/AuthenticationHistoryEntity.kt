package com.sseudam.storage.db.core.auth

import com.sseudam.auth.AuthenticationHistory
import com.sseudam.auth.Token
import com.sseudam.auth.TokenStatus
import com.sseudam.auth.command.AuthenticationHistoryCommand
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
        authenticationHistoryCommand: AuthenticationHistoryCommand,
    ) : this(
        userId = authenticationHistoryCommand.userId,
        userKey = authenticationHistoryCommand.userKey,
        deviceId = authenticationHistoryCommand.deviceId,
        accessToken = authenticationHistoryCommand.tokenGenerateCommand.token.accessToken,
        refreshToken = authenticationHistoryCommand.tokenGenerateCommand.token.refreshToken,
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
