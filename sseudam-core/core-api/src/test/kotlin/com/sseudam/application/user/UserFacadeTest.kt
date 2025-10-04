package com.sseudam.application.user

import com.sseudam.DevelopTest
import com.sseudam.auth.AuthenticationService
import com.sseudam.fixture.user.UserFixture
import com.sseudam.pet.UserPetService
import com.sseudam.user.UserFacade
import com.sseudam.user.UserService
import com.sseudam.user.command.UserWithdrawalCommand
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.verify

@DevelopTest
class UserFacadeTest :
    DescribeSpec({
        val userService: UserService = mockk()
        val authenticationService: AuthenticationService = mockk()
        val userPetService: UserPetService = mockk()
        val userFacade =
            UserFacade(
                userService = userService,
                authenticationService = authenticationService,
                userPetService = userPetService,
            )
        describe("사용자 탈퇴") {
            context("유효한 사용자인 경우") {
                it("트랜잭션 내에서 펫 삭제, 사용자 삭제, 인증 정보 삭제를 순차적으로 실행한다") {
                    val user = UserFixture.user
                    val withdrawalCommand = UserWithdrawalCommand(user)

                    every { userPetService.deleteByUser(user.id) } just Runs
                    every { userService.deleteUser(withdrawalCommand) } just Runs
                    every { authenticationService.withdrawUser(user.key) } just Runs

                    userFacade.withdrawalUser(user)

                    verify { userPetService.deleteByUser(user.id) }
                    verify { userService.deleteUser(withdrawalCommand) }
                    verify { authenticationService.withdrawUser(user.key) }
                }
            }

            context("펫 삭제 중 예외가 발생한 경우") {
                it("예외가 발생하고 트랜잭션이 롤백된다") {
                    val user = UserFixture.user

                    every { userPetService.deleteByUser(user.id) } throws RuntimeException("펫 삭제 실패")

                    try {
                        userFacade.withdrawalUser(user)
                    } catch (e: RuntimeException) {
                        e.message shouldBe "펫 삭제 실패"
                    }

                    verify { userPetService.deleteByUser(user.id) }
                }
            }

            context("사용자 삭제 중 예외가 발생한 경우") {
                it("예외가 발생하고 트랜잭션이 롤백된다") {
                    val user = UserFixture.user
                    val withdrawalCommand = UserWithdrawalCommand(user)

                    every { userPetService.deleteByUser(user.id) } just Runs
                    every { userService.deleteUser(withdrawalCommand) } throws RuntimeException("사용자 삭제 실패")

                    try {
                        userFacade.withdrawalUser(user)
                    } catch (e: RuntimeException) {
                        e.message shouldBe "사용자 삭제 실패"
                    }

                    verify { userPetService.deleteByUser(user.id) }
                    verify { userService.deleteUser(withdrawalCommand) }
                }
            }

            context("인증 정보 삭제 중 예외가 발생한 경우") {
                it("예외가 발생하고 트랜잭션이 롤백된다") {
                    val user = UserFixture.user
                    val withdrawalCommand = UserWithdrawalCommand(user)

                    every { userPetService.deleteByUser(user.id) } just Runs
                    every { userService.deleteUser(withdrawalCommand) } just Runs
                    every { authenticationService.withdrawUser(user.key) } throws RuntimeException("인증 정보 삭제 실패")

                    try {
                        userFacade.withdrawalUser(user)
                    } catch (e: RuntimeException) {
                        e.message shouldBe "인증 정보 삭제 실패"
                    }

                    verify { userPetService.deleteByUser(user.id) }
                    verify { userService.deleteUser(withdrawalCommand) }
                    verify { authenticationService.withdrawUser(user.key) }
                }
            }

            context("펫이 없는 사용자인 경우") {
                it("펫 삭제는 실행되지만 에러 없이 사용자 삭제와 인증 정보 삭제를 진행한다") {
                    val user = UserFixture.user
                    val withdrawalCommand = UserWithdrawalCommand(user)

                    every { userPetService.deleteByUser(user.id) } just Runs
                    every { userService.deleteUser(withdrawalCommand) } just Runs
                    every { authenticationService.withdrawUser(user.key) } just Runs

                    userFacade.withdrawalUser(user)

                    verify { userPetService.deleteByUser(user.id) }
                    verify { userService.deleteUser(withdrawalCommand) }
                    verify { authenticationService.withdrawUser(user.key) }
                }
            }

            context("작업 순서 검증") {
                it("펫 삭제 -> 사용자 삭제 -> 인증 정보 삭제 순서로 실행된다") {
                    val user = UserFixture.user
                    val withdrawalCommand = UserWithdrawalCommand(user)
                    val callOrder = mutableListOf<String>()

                    every { userPetService.deleteByUser(user.id) } answers { callOrder.add("pet") }
                    every { userService.deleteUser(withdrawalCommand) } answers { callOrder.add("user") }
                    every { authenticationService.withdrawUser(user.key) } answers { callOrder.add("auth") }

                    userFacade.withdrawalUser(user)

                    callOrder shouldBe listOf("pet", "user", "auth")
                }
            }
        }
    })
