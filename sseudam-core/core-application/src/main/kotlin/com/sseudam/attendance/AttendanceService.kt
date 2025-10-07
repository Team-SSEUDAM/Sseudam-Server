package com.sseudam.attendance

import com.sseudam.attendance.component.AttendanceAppender
import com.sseudam.attendance.component.AttendanceReader
import com.sseudam.attendance.result.AttendanceResult
import org.springframework.stereotype.Service
import java.time.LocalDate

@Service
class AttendanceService(
    private val attendanceAppender: AttendanceAppender,
    private val attendanceReader: AttendanceReader,
) {
    fun attendance(userId: Long): AttendanceResult {
        val currentDate = LocalDate.now()
        val attendance = attendanceReader.readLastByUser(userId)
        val isAlreadyAttendedToday = attendance?.date == currentDate

        if (isAlreadyAttendedToday) {
            return AttendanceResult.of(
                attendance!!.toComplete(isToday = true),
                attendance.continuity > 1,
                true,
            )
        }

        val continuity =
            if (attendance?.date?.plusDays(1) == currentDate && (attendance?.continuity ?: 0) < 5) {
                ((attendance?.continuity ?: 0) + 1).coerceAtMost(5)
            } else {
                1
            }
        val newAttendance = attendanceAppender.append(Attendance.Create(userId, currentDate, continuity))
        return AttendanceResult.of(
            newAttendance.toComplete(isToday = false),
            continuity > 1,
            false,
        )
    }
}
