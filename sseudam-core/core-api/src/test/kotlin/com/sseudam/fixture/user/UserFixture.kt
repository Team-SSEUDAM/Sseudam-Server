package com.sseudam.fixture.user

import com.navercorp.fixturemonkey.kotlin.setExp
import com.sseudam.fixture.common.AddressFixture
import com.sseudam.test.helper.fixtureBuilder
import com.sseudam.user.User
import com.sseudam.user.UserProfile
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
}
