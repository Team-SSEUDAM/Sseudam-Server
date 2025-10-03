package com.sseudam.application.auth

import com.sseudam.DevelopTest
import com.sseudam.auth.AuthenticationFacade
import com.sseudam.auth.AuthenticationService
import com.sseudam.fixture.auth.AuthFixture
import com.sseudam.fixture.user.UserFixture
import com.sseudam.user.SocialType
import com.sseudam.user.UserService
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify

@DevelopTest
class AuthenticationFacadeTest :
    DescribeSpec({
        val authenticationService: AuthenticationService = mockk()
        val userService: UserService = mockk()
        val authenticationFacade = AuthenticationFacade(userService, authenticationService)

        describe("소셜 로그인") {
            context("기존 사용자가 존재하고 이름이 있는 경우") {
                it("기존 사용자로 로그인하고 isNewUser는 false를 반환한다") {
                    val existingUser = AuthFixture.socialUser
                    val token = AuthFixture.token
                    val credentialSocial = AuthFixture.credentialSocialCommand

                    every { userService.getSocialUserByEmail("test@kakao.com") } returns existingUser
                    every { authenticationService.socialLogin("device123", existingUser) } returns token

                    val (isNewUser, resultToken) = authenticationFacade.socialLogin("device123", credentialSocial)

                    isNewUser shouldBe false
                    resultToken shouldBe token
                    verify { authenticationService.socialLogin("device123", existingUser) }
                }
            }

            context("기존 사용자가 존재하지만 이름이 비어있는 경우") {
                it("기존 사용자로 로그인하지만 isNewUser는 true를 반환한다") {
                    val existingUser = AuthFixture.socialNewUser
                    val token = AuthFixture.token
                    val credentialSocial = AuthFixture.credentialNewSocialCommand

                    every { userService.getSocialUserByEmail(credentialSocial.email) } returns existingUser
                    every { authenticationService.socialLogin("device123", existingUser) } returns token

                    val (isNewUser, resultToken) = authenticationFacade.socialLogin("device123", credentialSocial)

                    isNewUser shouldBe true
                    resultToken shouldBe token
                }
            }

            context("기존 사용자가 존재하지만 이름이 null인 경우") {
                it("기존 사용자로 로그인하지만 isNewUser는 true를 반환한다") {
                    val existingUser = AuthFixture.socialNewUser
                    val token = AuthFixture.token
                    val credentialSocial = AuthFixture.credentialNewSocialCommand

                    every { userService.getSocialUserByEmail(credentialSocial.email) } returns existingUser
                    every { authenticationService.socialLogin("device123", existingUser) } returns token

                    val (isNewUser, resultToken) = authenticationFacade.socialLogin("device123", credentialSocial)

                    isNewUser shouldBe true
                    resultToken shouldBe token
                }
            }

            context("신규 사용자인 경우") {
                it("새로운 사용자를 생성하고 로그인하며 isNewUser는 true를 반환한다") {
                    val newUser = UserFixture.user
                    val token = AuthFixture.token
                    val credentialSocial = AuthFixture.credentialNewSocialCommand

                    every { userService.getSocialUserByEmail("test@apple.com") } returns null
                    every { userService.create(any()) } returns newUser
                    every { authenticationService.socialLogin("device123", any()) } returns token

                    val (isNewUser, resultToken) = authenticationFacade.socialLogin("device123", credentialSocial)

                    isNewUser shouldBe true
                    resultToken shouldBe token
                    verify { userService.create(any()) }
                    verify { authenticationService.socialLogin("device123", any()) }
                }
            }

            context("deviceId가 빈 문자열인 경우") {
                it("정상적으로 로그인을 처리한다") {
                    val existingUser = AuthFixture.socialUser
                    val token = AuthFixture.token
                    val credentialSocial = AuthFixture.credentialSocialCommand

                    every { userService.getSocialUserByEmail("test@kakao.com") } returns existingUser
                    every { authenticationService.socialLogin("", existingUser) } returns token

                    val (isNewUser, resultToken) = authenticationFacade.socialLogin("", credentialSocial)

                    isNewUser shouldBe false
                    resultToken shouldBe token
                }
            }
        }

        describe("신규 소셜 사용자 생성") {
            it("새로운 소셜 사용자를 생성하고 isUserNew는 true를 반환한다") {
                val newUser = UserFixture.user
                val credentialSocial = AuthFixture.credentialNewSocialCommand

                every { userService.create(any()) } returns newUser

                val (socialUser, isUserNew) = authenticationFacade.createNewSocialUser(credentialSocial)

                isUserNew shouldBe true
                socialUser.id shouldBe 1L
                socialUser.key shouldBe "20251003_UK_d69fc385032246949285908cf6588a41"
                socialUser.name shouldBe ""
                socialUser.socialId shouldBe "socialId123"
                socialUser.socialType shouldBe SocialType.APPLE
                verify { userService.create(any()) }
            }
        }
    })
