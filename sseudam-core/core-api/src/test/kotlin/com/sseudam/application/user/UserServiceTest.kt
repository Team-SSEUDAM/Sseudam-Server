package com.sseudam.application.user

import com.sseudam.DevelopTest
import com.sseudam.fixture.common.PageFixture
import com.sseudam.fixture.user.UserFixture
import com.sseudam.support.page.Page
import com.sseudam.user.UserProfile
import com.sseudam.user.UserService
import com.sseudam.user.command.UserCommand
import com.sseudam.user.component.UserAppender
import com.sseudam.user.component.UserDeleter
import com.sseudam.user.component.UserReader
import com.sseudam.user.component.UserUpdater
import com.sseudam.user.component.UserValidator
import com.sseudam.user.event.UserSignUpEvent
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.verify
import org.springframework.context.ApplicationEventPublisher

@DevelopTest
class UserServiceTest :
    DescribeSpec({
        val userAppender: UserAppender = mockk()
        val userReader: UserReader = mockk()
        val userUpdater: UserUpdater = mockk()
        val userDeleter: UserDeleter = mockk()
        val userValidator: UserValidator = mockk()
        val applicationEventPublisher: ApplicationEventPublisher = mockk()
        val userService =
            UserService(
                userAppender = userAppender,
                userReader = userReader,
                userUpdater = userUpdater,
                userDeleter = userDeleter,
                userValidator = userValidator,
                applicationEventPublisher = applicationEventPublisher,
            )

        describe("사용자 생성") {
            context("유효한 사용자 정보인 경우") {
                it("사용자를 생성하고 반환한다") {
                    val userCommand = UserFixture.userCommand
                    val user = UserFixture.user

                    every { userValidator.verifyEmail(userCommand.email) } just Runs
                    every { userAppender.create(userCommand) } returns user

                    val result = userService.create(userCommand)

                    result shouldBe user
                    verify { userValidator.verifyEmail(userCommand.email) }
                    verify { userAppender.create(userCommand) }
                }
            }
        }

        describe("소셜 회원가입") {
            context("주소 정보가 있는 경우") {
                it("이름과 주소를 업데이트하고 이벤트를 발행한다") {
                    val socialUser = UserFixture.socialUser
                    val userCommand = UserFixture.userCommand
                    val userProfile = UserFixture.userProfile

                    every { userValidator.verifyNickname(userCommand.name) } just Runs
                    every { userUpdater.updateName(socialUser.key, userCommand.name) } returns userProfile
                    every { userUpdater.updateAddress(socialUser.key, userCommand.address!!) } returns userProfile
                    every { applicationEventPublisher.publishEvent(any<UserSignUpEvent>()) } just Runs

                    userService.socialSignUp(socialUser, userCommand)

                    verify { userUpdater.updateName(socialUser.key, userCommand.name) }
                    verify { userUpdater.updateAddress(socialUser.key, userCommand.address!!) }
                    verify { applicationEventPublisher.publishEvent(UserSignUpEvent(socialUser.id)) }
                }
            }

            context("주소 정보가 없는 경우") {
                it("이름만 업데이트하고 이벤트를 발행한다") {
                    val socialUser = UserFixture.socialUser
                    val userCommand: UserCommand = UserFixture.userCommand
                    val userProfile = UserFixture.userProfile
                    val name = "홍길동"

                    every { userValidator.verifyNickname(name) } just Runs
                    every { userUpdater.updateName(socialUser.key, name) } returns userProfile
                    every { applicationEventPublisher.publishEvent(any<UserSignUpEvent>()) } just Runs

                    userService.socialSignUp(socialUser, userCommand)

                    verify { userValidator.verifyNickname(name) }
                    verify { userUpdater.updateName(socialUser.key, name) }
                    verify(exactly = 0) { userUpdater.updateAddress(userProfile.key, userProfile.address) }
                    verify { applicationEventPublisher.publishEvent(UserSignUpEvent(socialUser.id)) }
                }
            }
        }

        describe("프로필 조회") {
            context("존재하는 사용자인 경우") {
                it("사용자 프로필을 반환한다") {
                    val userId = 1L
                    val userProfile = UserFixture.userProfile

                    every { userReader.readUserProfile(userId) } returns userProfile

                    val result = userService.getProfile(userId)

                    result shouldBe userProfile
                    verify { userReader.readUserProfile(userId) }
                }
            }

            context("존재하지 않는 사용자인 경우") {
                it("null을 반환한다") {
                    val userId = 999L

                    every { userReader.readUserProfile(userId) } returns null

                    val result = userService.getProfile(userId)

                    result shouldBe null
                }
            }
        }

        describe("이메일로 소셜 사용자 조회") {
            context("존재하는 이메일인 경우") {
                it("소셜 사용자를 반환한다") {
                    val email = "test@example.com"
                    val socialUser = UserFixture.socialUser

                    every { userReader.readUserByEmail(email) } returns socialUser

                    val result = userService.getSocialUserByEmail(email)

                    result shouldBe socialUser
                }
            }

            context("존재하지 않는 이메일인 경우") {
                it("null을 반환한다") {
                    val email = "notfound@example.com"

                    every { userReader.readUserByEmail(email) } returns null

                    val result = userService.getSocialUserByEmail(email)

                    result shouldBe null
                }
            }
        }

        describe("닉네임 업데이트") {
            context("유효한 닉네임인 경우") {
                it("닉네임을 업데이트하고 프로필을 반환한다") {
                    val userKey = "userKey123"
                    val updateCommand = UserFixture.updateNicknameCommand
                    val userProfile = UserFixture.userProfile

                    every { userValidator.verifyNickname(updateCommand.nickname) } just Runs
                    every { userUpdater.updateNickname(userKey, updateCommand) } returns userProfile

                    val result = userService.updateNickname(userKey, updateCommand)

                    result shouldBe userProfile
                    verify { userValidator.verifyNickname(updateCommand.nickname) }
                    verify { userUpdater.updateNickname(userKey, updateCommand) }
                }
            }
        }

        describe("이메일 검증") {
            context("사용 가능한 이메일인 경우") {
                it("예외를 던지지 않는다") {
                    val email = "available@example.com"

                    every { userValidator.verifyEmail(email) } just Runs

                    userService.checkEmail(email)

                    verify { userValidator.verifyEmail(email) }
                }
            }
        }

        describe("닉네임 검증") {
            context("유효한 닉네임인 경우") {
                it("true를 반환한다") {
                    val nickname = "유효한닉네임"

                    every { userValidator.verifyNickname(nickname) } just Runs

                    val result = userService.validateNickname(nickname)

                    result shouldBe true
                    verify { userValidator.verifyNickname(nickname) }
                }
            }
        }

        describe("사용자 탈퇴") {
            context("유효한 사용자인 경우") {
                it("사용자를 삭제한다") {
                    val withdrawalCommand = UserFixture.userWithdrawalCommand

                    every { userDeleter.deleteUser(withdrawalCommand.user.key) } just Runs

                    userService.deleteUser(withdrawalCommand)

                    verify { userDeleter.deleteUser(withdrawalCommand.user.key) }
                }
            }
        }

        describe("페이징 조회") {
            context("페이지 요청이 있는 경우") {
                it("페이징된 사용자 프로필을 반환한다") {
                    val offsetPageRequest = PageFixture.offsetPageRequest
                    val userProfile = UserFixture.userProfile
                    val page = Page(listOf(userProfile), 1)

                    every { userReader.readAllBy(offsetPageRequest) } returns page

                    val result = userService.findUserProfileBy(offsetPageRequest)

                    result shouldBe page
                    verify { userReader.readAllBy(offsetPageRequest) }
                }
            }
        }

        describe("여러 사용자 조회") {
            context("사용자 ID 목록이 주어진 경우") {
                it("해당하는 사용자 프로필 목록을 반환한다") {
                    val userIds = listOf(1L, 2L, 3L)
                    val userProfile = UserFixture.userProfile
                    val userProfiles = listOf(userProfile)

                    every { userReader.readAllByUserIds(userIds) } returns userProfiles

                    val result = userService.findAllBy(userIds)

                    result shouldBe userProfiles
                    verify { userReader.readAllByUserIds(userIds) }
                }
            }

            context("빈 ID 목록인 경우") {
                it("빈 목록을 반환한다") {
                    val userIds = emptyList<Long>()
                    val userProfiles = emptyList<UserProfile>()

                    every { userReader.readAllByUserIds(userIds) } returns userProfiles

                    val result = userService.findAllBy(userIds)

                    result shouldBe userProfiles
                }
            }
        }
    })
