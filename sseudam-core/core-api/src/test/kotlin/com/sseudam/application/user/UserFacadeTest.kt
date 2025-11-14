package com.sseudam.application.user

import com.sseudam.DevelopTest
import com.sseudam.fixture.user.UserFixture
import com.sseudam.pet.UserPetService
import com.sseudam.user.UserFacade
import com.sseudam.user.UserService
import com.sseudam.user.command.UserWithdrawalCommand
import com.sseudam.user.event.UserWithdrawalEvent
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.slot
import io.mockk.verify
import org.springframework.context.ApplicationEventPublisher

@DevelopTest
class UserFacadeTest :
    DescribeSpec({
        val userService: UserService = mockk()
        val userPetService: UserPetService = mockk()
        val eventPublisher: ApplicationEventPublisher = mockk()
        val userFacade =
            UserFacade(
                userService = userService,
                userPetService = userPetService,
                eventPublisher = eventPublisher,
            )
        describe("사용자 탈퇴") {
            context("유효한 사용자인 경우") {
                it("트랜잭션 내에서 펫 삭제, 사용자 삭제, 탈퇴 이벤트 발행을 순차적으로 실행한다") {
                    val user = UserFixture.user
                    val withdrawalCommand = UserWithdrawalCommand(user)
                    val eventSlot = slot<UserWithdrawalEvent>()

                    every { userPetService.deleteByUser(user.id) } just Runs
                    every { userService.deleteUser(withdrawalCommand) } just Runs
                    every { eventPublisher.publishEvent(capture(eventSlot)) } just Runs

                    userFacade.withdrawalUser(user)

                    verify { userPetService.deleteByUser(user.id) }
                    verify { userService.deleteUser(withdrawalCommand) }
                    verify { eventPublisher.publishEvent(any<UserWithdrawalEvent>()) }
                    eventSlot.captured.userKey shouldBe user.key
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

            context("이벤트 발행 중 예외가 발생한 경우") {
                it("예외가 발생하고 트랜잭션이 롤백된다") {
                    val user = UserFixture.user
                    val withdrawalCommand = UserWithdrawalCommand(user)

                    every { userPetService.deleteByUser(user.id) } just Runs
                    every { userService.deleteUser(withdrawalCommand) } just Runs
                    every { eventPublisher.publishEvent(any<UserWithdrawalEvent>()) } throws RuntimeException("이벤트 발행 실패")

                    try {
                        userFacade.withdrawalUser(user)
                    } catch (e: RuntimeException) {
                        e.message shouldBe "이벤트 발행 실패"
                    }

                    verify { userPetService.deleteByUser(user.id) }
                    verify { userService.deleteUser(withdrawalCommand) }
                    verify { eventPublisher.publishEvent(any<UserWithdrawalEvent>()) }
                }
            }

            context("펫이 없는 사용자인 경우") {
                it("펫 삭제는 실행되지만 에러 없이 사용자 삭제와 이벤트 발행을 진행한다") {
                    val user = UserFixture.user
                    val withdrawalCommand = UserWithdrawalCommand(user)

                    every { userPetService.deleteByUser(user.id) } just Runs
                    every { userService.deleteUser(withdrawalCommand) } just Runs
                    every { eventPublisher.publishEvent(any<UserWithdrawalEvent>()) } just Runs

                    userFacade.withdrawalUser(user)

                    verify { userPetService.deleteByUser(user.id) }
                    verify { userService.deleteUser(withdrawalCommand) }
                    verify { eventPublisher.publishEvent(any<UserWithdrawalEvent>()) }
                }
            }

            context("작업 순서 검증") {
                it("펫 삭제 -> 사용자 삭제 -> 이벤트 발행 순서로 실행된다") {
                    val user = UserFixture.user
                    val withdrawalCommand = UserWithdrawalCommand(user)
                    val callOrder = mutableListOf<String>()

                    every { userPetService.deleteByUser(user.id) } answers { callOrder.add("pet") }
                    every { userService.deleteUser(withdrawalCommand) } answers { callOrder.add("user") }
                    every { eventPublisher.publishEvent(any<UserWithdrawalEvent>()) } answers { callOrder.add("event") }

                    userFacade.withdrawalUser(user)

                    callOrder shouldBe listOf("pet", "user", "event")
                }
            }
        }
    })
