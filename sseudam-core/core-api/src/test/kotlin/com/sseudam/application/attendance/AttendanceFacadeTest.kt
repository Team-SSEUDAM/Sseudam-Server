package com.sseudam.application.attendance

import com.sseudam.DevelopTest
import com.sseudam.attendance.AttendanceFacade
import com.sseudam.attendance.AttendanceService
import com.sseudam.fixture.attendance.AttendanceFixture
import com.sseudam.pet.PetPointAction
import com.sseudam.pet.event.UserPetContextEvent
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.verify
import org.springframework.context.ApplicationEventPublisher

@DevelopTest
class AttendanceFacadeTest :
    DescribeSpec({
        val attendanceService: AttendanceService = mockk()
        val applicationEventPublisher: ApplicationEventPublisher = mockk()
        val attendanceFacade = AttendanceFacade(attendanceService, applicationEventPublisher)

        describe("오늘 출석") {
            context("첫 출석인 경우") {
                it("출석을 생성하고 ATTENDANCE 이벤트를 발행한다") {
                    val userId = 1L
                    val attendanceResult = AttendanceFixture.attendanceResult.copy(isToday = false, continuity = 1, isContinuity = false)

                    every { attendanceService.attendance(userId) } returns attendanceResult
                    every { applicationEventPublisher.publishEvent(any<UserPetContextEvent>()) } just Runs

                    val result = attendanceFacade.todayAttendance(userId)

                    result shouldBe attendanceResult
                    verify { attendanceService.attendance(userId) }
                    verify {
                        applicationEventPublisher.publishEvent(
                            match<UserPetContextEvent> {
                                it.userId == userId && it.petPointAction == PetPointAction.ATTENDANCE
                            },
                        )
                    }
                }
            }

            context("연속 출석인 경우") {
                it("출석을 생성하고 ATTENDANCE 이벤트를 발행한다") {
                    val userId = 1L
                    val attendanceResult = AttendanceFixture.attendanceResult.copy(isToday = false, continuity = 3, isContinuity = true)

                    every { attendanceService.attendance(userId) } returns attendanceResult
                    every { applicationEventPublisher.publishEvent(any<UserPetContextEvent>()) } just Runs

                    val result = attendanceFacade.todayAttendance(userId)

                    result shouldBe attendanceResult
                    verify { attendanceService.attendance(userId) }
                    verify {
                        applicationEventPublisher.publishEvent(
                            match<UserPetContextEvent> {
                                it.userId == userId && it.petPointAction == PetPointAction.ATTENDANCE
                            },
                        )
                    }
                }
            }

            context("5일 연속 출석인 경우") {
                it("출석을 생성하고 CONTINUITY_ATTENDANCE 이벤트를 발행한다") {
                    val userId = 1L
                    val attendanceResult = AttendanceFixture.attendanceResult.copy(isToday = false, continuity = 5, isContinuity = true)

                    every { attendanceService.attendance(userId) } returns attendanceResult
                    every { applicationEventPublisher.publishEvent(any<UserPetContextEvent>()) } just Runs

                    val result = attendanceFacade.todayAttendance(userId)

                    result shouldBe attendanceResult
                    verify { attendanceService.attendance(userId) }
                    verify {
                        applicationEventPublisher.publishEvent(
                            match<UserPetContextEvent> {
                                it.userId == userId && it.petPointAction == PetPointAction.CONTINUITY_ATTENDANCE
                            },
                        )
                    }
                }
            }

            context("오늘 이미 출석한 경우") {
                it("기존 출석 정보를 반환하고 이벤트를 발행하지 않는다") {
                    val userId = 1L
                    val attendanceResult = AttendanceFixture.attendanceResult.copy(isToday = true, continuity = 3, isContinuity = true)

                    every { attendanceService.attendance(userId) } returns attendanceResult

                    val result = attendanceFacade.todayAttendance(userId)

                    result shouldBe attendanceResult
                    verify { attendanceService.attendance(userId) }
                }
            }
        }
    })
