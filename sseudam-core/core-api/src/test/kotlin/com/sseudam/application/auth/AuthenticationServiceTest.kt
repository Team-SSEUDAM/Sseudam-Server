package com.sseudam.application.auth

import com.sseudam.DevelopTest
import com.sseudam.auth.AuthenticationService
import com.sseudam.auth.AuthorityType
import com.sseudam.auth.Token
import com.sseudam.auth.command.CredentialSseudamCommand
import com.sseudam.auth.component.AuthenticationProcessor
import com.sseudam.auth.dto.GrantedAuthority
import com.sseudam.user.SocialType
import com.sseudam.user.SocialUser
import com.sseudam.user.User
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify

@DevelopTest
class AuthenticationServiceTest :
    DescribeSpec({
        val authenticationProcessor: AuthenticationProcessor = mockk()
        val authenticationService = AuthenticationService(authenticationProcessor)

        describe("일반 로그인") {
            context("유효한 사용자와 deviceId가 있는 경우") {
                it("토큰을 생성하고 반환한다") {
                    val user = User(1L, "userKey")
                    val credential = CredentialSseudamCommand("test@test.com", "password")
                    val token = Token("accessToken", "refreshToken")

                    every { authenticationProcessor.login("device123", user, credential) } returns token

                    val result = authenticationService.login("device123", user, credential)

                    result shouldBe token
                    verify { authenticationProcessor.login("device123", user, credential) }
                }
            }

            context("deviceId가 null인 경우") {
                it("null deviceId로 토큰을 생성한다") {
                    val user = User(1L, "userKey")
                    val credential = CredentialSseudamCommand("test@test.com", "password")
                    val token = Token("accessToken", "refreshToken")

                    every { authenticationProcessor.login(null, user, credential) } returns token

                    val result = authenticationService.login(null, user, credential)

                    result shouldBe token
                    verify { authenticationProcessor.login(null, user, credential) }
                }
            }
        }

        describe("소셜 로그인") {
            context("유효한 소셜 사용자인 경우") {
                it("토큰을 생성하고 반환한다") {
                    val socialUser = SocialUser(1L, "userKey", "카카오유저", "socialId123", SocialType.KAKAO)
                    val token = Token("accessToken", "refreshToken")

                    every { authenticationProcessor.login("device123", socialUser) } returns token

                    val result = authenticationService.socialLogin("device123", socialUser)

                    result shouldBe token
                    verify { authenticationProcessor.login("device123", socialUser) }
                }
            }

            context("빈 deviceId인 경우") {
                it("빈 deviceId로 토큰을 생성한다") {
                    val socialUser = SocialUser(1L, "userKey", "애플유저", "socialId456", SocialType.APPLE)
                    val token = Token("accessToken", "refreshToken")

                    every { authenticationProcessor.login("", socialUser) } returns token

                    val result = authenticationService.socialLogin("", socialUser)

                    result shouldBe token
                }
            }
        }

        describe("토큰 재발급") {
            context("유효한 refreshToken인 경우") {
                it("새로운 토큰을 반환한다") {
                    val refreshToken = "validRefreshToken"
                    val newToken = Token("newAccessToken", "newRefreshToken")

                    every { authenticationProcessor.renew(refreshToken) } returns newToken

                    val result = authenticationService.renew(refreshToken)

                    result shouldBe newToken
                    verify { authenticationProcessor.renew(refreshToken) }
                }
            }

            context("빈 refreshToken인 경우") {
                it("processor를 호출한다") {
                    val refreshToken = ""
                    val token = Token("accessToken", "refreshToken")

                    every { authenticationProcessor.renew(refreshToken) } returns token

                    val result = authenticationService.renew(refreshToken)

                    result shouldBe token
                }
            }
        }

        describe("로그아웃") {
            context("유효한 토큰인 경우") {
                it("userKey를 반환한다") {
                    val token = "validToken"
                    val userKey = "userKey123"

                    every { authenticationProcessor.remove(token) } returns userKey

                    val result = authenticationService.logout(token)

                    result shouldBe userKey
                    verify { authenticationProcessor.remove(token) }
                }
            }

            context("빈 토큰인 경우") {
                it("processor를 호출한다") {
                    val token = ""
                    val userKey = "userKey"

                    every { authenticationProcessor.remove(token) } returns userKey

                    val result = authenticationService.logout(token)

                    result shouldBe userKey
                }
            }
        }

        describe("회원 탈퇴") {
            context("유효한 userKey인 경우") {
                it("토큰을 제거한다") {
                    val userKey = "userKey123"

                    every { authenticationProcessor.withdrawal(userKey) } returns Unit

                    authenticationService.withdrawUser(userKey)

                    verify { authenticationProcessor.withdrawal(userKey) }
                }
            }

            context("빈 userKey인 경우") {
                it("processor를 호출한다") {
                    val userKey = ""

                    every { authenticationProcessor.withdrawal(userKey) } returns Unit

                    authenticationService.withdrawUser(userKey)

                    verify { authenticationProcessor.withdrawal(userKey) }
                }
            }
        }

        describe("관리자 로그인") {
            context("유효한 adminId인 경우") {
                it("관리자 권한으로 토큰을 생성한다") {
                    val adminId = 1L
                    val token = Token("adminAccessToken", "adminRefreshToken")
                    val authorities = listOf(GrantedAuthority(AuthorityType.ADMIN))

                    every { authenticationProcessor.adminLogin(adminId, authorities) } returns token

                    val result = authenticationService.adminLogin(adminId)

                    result shouldBe token
                    verify { authenticationProcessor.adminLogin(adminId, authorities) }
                }
            }

            context("adminId가 0인 경우") {
                it("processor를 호출한다") {
                    val adminId = 0L
                    val token = Token("accessToken", "refreshToken")

                    every { authenticationProcessor.adminLogin(adminId, any()) } returns token

                    val result = authenticationService.adminLogin(adminId)

                    result shouldBe token
                }
            }
        }

        describe("관리자 토큰 재발급") {
            context("유효한 refreshToken인 경우") {
                it("새로운 관리자 토큰을 반환한다") {
                    val refreshToken = "adminRefreshToken"
                    val newToken = Token("newAdminAccessToken", "newAdminRefreshToken")

                    every { authenticationProcessor.adminRenew(refreshToken) } returns newToken

                    val result = authenticationService.adminReissue(refreshToken)

                    result shouldBe newToken
                    verify { authenticationProcessor.adminRenew(refreshToken) }
                }
            }
        }

        describe("관리자 로그아웃") {
            context("유효한 accessToken인 경우") {
                it("관리자 토큰을 제거한다") {
                    val accessToken = "adminAccessToken"

                    every { authenticationProcessor.adminLogout(accessToken) } returns Unit

                    authenticationService.adminLogout(accessToken)

                    verify { authenticationProcessor.adminLogout(accessToken) }
                }
            }

            context("빈 accessToken인 경우") {
                it("processor를 호출한다") {
                    val accessToken = ""

                    every { authenticationProcessor.adminLogout(accessToken) } returns Unit

                    authenticationService.adminLogout(accessToken)

                    verify { authenticationProcessor.adminLogout(accessToken) }
                }
            }
        }
    })
