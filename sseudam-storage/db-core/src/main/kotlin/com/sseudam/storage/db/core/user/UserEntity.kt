package com.sseudam.storage.db.core.user

import com.sseudam.common.Address
import com.sseudam.storage.db.core.support.BaseEntity
import com.sseudam.user.SocialType
import com.sseudam.user.SocialUser
import com.sseudam.user.User
import com.sseudam.user.UserCredentials
import com.sseudam.user.UserProfile
import com.sseudam.user.command.UserCommand
import com.sseudam.user.command.UserKeyCommand
import jakarta.persistence.Column
import jakarta.persistence.Embedded
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.Table

@Entity
@Table(name = "t_users")
class UserEntity(
    @Column(name = "user_key")
    val userKey: String,
    var name: String?,
    var nickname: String?,
    @Embedded
    var address: Address,
    var email: String,
    val password: String?,
    var socialId: String?,
    @Enumerated(value = EnumType.STRING)
    @Column(length = 50)
    val socialType: SocialType,
) : BaseEntity() {
    constructor(
        userCommand: UserCommand,
        userKeyCommand: UserKeyCommand,
    ) : this(
        userKey = userKeyCommand.key,
        email = userCommand.email,
        name = userCommand.name,
        nickname = userCommand.nickname,
        address =
            userCommand.address ?: Address(
                city = "",
                site = "",
            ),
        password = userCommand.password,
        socialId = userCommand.socialId,
        socialType = userCommand.socialType,
    )

    fun toUser(): User =
        User(
            id = id!!,
            key = userKey,
        )

    fun toSocialUser(): SocialUser =
        SocialUser(
            id = id!!,
            key = userKey,
            name = name,
            socialId = socialId!!,
            socialType = socialType,
        )

    fun toProfile(): UserProfile =
        UserProfile(
            id = id!!,
            key = userKey,
            email = email,
            name = name,
            nickname = nickname ?: "",
            address = address,
            createdAt = createdAt,
        )

    fun toUserCredentials(): UserCredentials =
        UserCredentials(
            id = id!!,
            key = userKey,
            loginId = email,
            password = password!!,
        )

    fun updateNickname(nickname: String) {
        this.nickname = nickname
    }

    fun updateName(name: String) {
        this.name = name
    }

    fun updateEmail(email: String) {
        this.email = email
    }

    fun updateAddress(address: Address) {
        this.address = address
    }
}
