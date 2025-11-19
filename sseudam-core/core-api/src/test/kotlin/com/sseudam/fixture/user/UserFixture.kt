package com.sseudam.fixture.user

import com.navercorp.fixturemonkey.kotlin.setExp
import com.sseudam.common.SocialType
import com.sseudam.fixture.common.AddressFixture
import com.sseudam.test.helper.fixtureBuilder
import com.sseudam.user.SocialUser
import com.sseudam.user.User
import com.sseudam.user.UserProfile
import com.sseudam.user.command.UpdateNicknameCommand
import com.sseudam.user.command.UserCommand
import com.sseudam.user.command.UserWithdrawalCommand
import java.time.LocalDateTime

object UserFixture {
    val user =
        fixtureBuilder<User> {
            setExp(User::id, 1L)
            setExp(User::key, "20251003_UK_d69fc385032246949285908cf6588a41")
        }

    val userProfile =
        fixtureBuilder<UserProfile> {
            setExp(UserProfile::id, 1L)
            setExp(UserProfile::key, "20251003_UK_d69fc385032246949285908cf6588a41")
            setExp(UserProfile::email, "test@test.com")
            setExp(UserProfile::name, "Test User")
            setExp(UserProfile::nickname, "TestNickname")
            setExp(UserProfile::address, AddressFixture.addressFixture)
            setExp(UserProfile::createdAt, LocalDateTime.now())
        }

    val userCommand =
        fixtureBuilder<UserCommand> {
            setExp(UserCommand::email, "test@example.com")
            setExp(UserCommand::name, "홍길동")
            setExp(UserCommand::address, AddressFixture.addressFixture)
        }

    val socialUser =
        fixtureBuilder<SocialUser> {
            setExp(SocialUser::id, 1L)
            setExp(SocialUser::key, "userKey123")
            setExp(SocialUser::name, "카카오유저")
            setExp(SocialUser::socialId, "socialId123")
            setExp(SocialUser::socialType, SocialType.KAKAO)
        }

    val updateNicknameCommand =
        fixtureBuilder<UpdateNicknameCommand> {
            setExp(UpdateNicknameCommand::nickname, "새닉네임")
        }

    val userWithdrawalCommand =
        fixtureBuilder<UserWithdrawalCommand> {
            setExp(UserWithdrawalCommand::user, user)
        }
}
