package com.sseudam.fixture.attendance

import com.navercorp.fixturemonkey.kotlin.setExp
import com.sseudam.attendance.Attendance
import com.sseudam.attendance.result.AttendanceResult
import com.sseudam.test.helper.fixtureBuilder
import java.time.LocalDate
import java.time.LocalDateTime

object AttendanceFixture {
    val attendanceResult =
        fixtureBuilder<AttendanceResult> {
            setExp(AttendanceResult::userId, 1L)
            setExp(AttendanceResult::date, LocalDate.now())
            setExp(AttendanceResult::isToday, true)
            setExp(AttendanceResult::continuity, 3)
            setExp(AttendanceResult::isContinuity, true)
            setExp(AttendanceResult::createdAt, LocalDateTime.now())
        }

    val attendanceInfo =
        fixtureBuilder<Attendance.Info> {
            setExp(Attendance.Info::id, 1L)
            setExp(Attendance.Info::userId, 1L)
            setExp(Attendance.Info::date, LocalDate.now())
            setExp(Attendance.Info::continuity, 3)
            setExp(Attendance.Info::createdAt, LocalDateTime.now())
        }
}
