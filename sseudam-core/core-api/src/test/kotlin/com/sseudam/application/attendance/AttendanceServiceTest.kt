package com.sseudam.application.attendance

import com.sseudam.DevelopTest
import com.sseudam.attendance.AttendanceService
import com.sseudam.attendance.component.AttendanceAppender
import com.sseudam.attendance.component.AttendanceReader
import com.sseudam.fixture.attendance.AttendanceFixture
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import java.time.LocalDate

@DevelopTest
class AttendanceServiceTest :
    DescribeSpec({
        val attendanceAppender: AttendanceAppender = mockk()
        val attendanceReader: AttendanceReader = mockk()

        val attendanceService = AttendanceService(attendanceAppender, attendanceReader)

        describe("출석 체크") {
            context("첫 출석인 경우") {
                it("연속 출석 1일로 출석을 생성한다") {
                    val userId = 1L
                    val newAttendance = AttendanceFixture.attendanceInfo.copy(continuity = 1)

                    every { attendanceReader.readLastByUser(userId) } returns null
                    every { attendanceAppender.append(any()) } returns newAttendance

                    val result = attendanceService.attendance(userId)

                    result.userId shouldBe userId
                    result.continuity shouldBe 1
                    result.isToday shouldBe false
                    result.isContinuity shouldBe false
                    verify { attendanceReader.readLastByUser(userId) }
                    verify { attendanceAppender.append(any()) }
                }
            }

            context("연속 출석인 경우") {
                it("연속 출석 일수를 증가시킨다") {
                    val userId = 1L
                    val yesterday = LocalDate.now().minusDays(1)
                    val lastAttendance = AttendanceFixture.attendanceInfo.copy(date = yesterday, continuity = 2)
                    val newAttendance = AttendanceFixture.attendanceInfo.copy(continuity = 3)

                    every { attendanceReader.readLastByUser(userId) } returns lastAttendance
                    every { attendanceAppender.append(any()) } returns newAttendance

                    val result = attendanceService.attendance(userId)

                    result.userId shouldBe userId
                    result.continuity shouldBe 3
                    result.isToday shouldBe false
                    result.isContinuity shouldBe true
                    verify { attendanceReader.readLastByUser(userId) }
                    verify { attendanceAppender.append(any()) }
                }
            }

            context("연속 출석이 끊긴 경우") {
                it("연속 출석 1일로 초기화한다") {
                    val userId = 1L
                    val twoDaysAgo = LocalDate.now().minusDays(2)
                    val lastAttendance = AttendanceFixture.attendanceInfo.copy(date = twoDaysAgo, continuity = 3)
                    val newAttendance = AttendanceFixture.attendanceInfo.copy(continuity = 1)

                    every { attendanceReader.readLastByUser(userId) } returns lastAttendance
                    every { attendanceAppender.append(any()) } returns newAttendance

                    val result = attendanceService.attendance(userId)

                    result.userId shouldBe userId
                    result.continuity shouldBe 1
                    result.isToday shouldBe false
                    result.isContinuity shouldBe false
                    verify { attendanceReader.readLastByUser(userId) }
                    verify { attendanceAppender.append(any()) }
                }
            }

            context("연속 출석이 5일인 경우") {
                it("연속 출석 5일을 유지한다") {
                    val userId = 1L
                    val yesterday = LocalDate.now().minusDays(1)
                    val lastAttendance = AttendanceFixture.attendanceInfo.copy(date = yesterday, continuity = 5)
                    val newAttendance = AttendanceFixture.attendanceInfo.copy(continuity = 5)

                    every { attendanceReader.readLastByUser(userId) } returns lastAttendance
                    every { attendanceAppender.append(any()) } returns newAttendance

                    val result = attendanceService.attendance(userId)

                    result.userId shouldBe userId
                    result.continuity shouldBe 5
                    result.isToday shouldBe false
                    result.isContinuity shouldBe false
                    verify { attendanceReader.readLastByUser(userId) }
                    verify { attendanceAppender.append(any()) }
                }
            }

            context("오늘 이미 출석한 경우") {
                it("기존 출석 정보를 반환한다") {
                    val userId = 1L
                    val today = LocalDate.now()
                    val todayAttendance = AttendanceFixture.attendanceInfo.copy(date = today, continuity = 3)

                    every { attendanceReader.readLastByUser(userId) } returns todayAttendance

                    val result = attendanceService.attendance(userId)

                    result.userId shouldBe userId
                    result.continuity shouldBe 3
                    result.isToday shouldBe true
                    result.isContinuity shouldBe true
                    verify { attendanceReader.readLastByUser(userId) }
                }
            }
        }
    })
