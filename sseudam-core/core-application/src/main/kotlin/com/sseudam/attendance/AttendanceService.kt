package com.sseudam.attendance

import com.sseudam.attendance.component.AttendanceAppender
import com.sseudam.attendance.component.AttendanceReader
import org.springframework.stereotype.Service
import java.time.LocalDate

@Service
class AttendanceService(
    private val attendanceAppender: AttendanceAppender,
    private val attendanceReader: AttendanceReader,
) {
    fun attendance(userId: Long): Triple<Boolean, Attendance.Complete, Boolean> {
        val currentDate = LocalDate.now()
        val attendance = attendanceReader.readLastByUser(userId)
        val isFirstAttendanceToday = attendance?.date == currentDate

        if (attendance == null || !isFirstAttendanceToday) {
            val continuity =
                if (attendance?.date?.plusDays(1) == currentDate && (attendance?.continuity ?: 0) < 5) {
                    ((attendance?.continuity ?: 0) + 1).coerceAtMost(5)
                } else {
                    1
                }
            val newAttendance = attendanceAppender.append(Attendance.Create(userId, currentDate, continuity))
            return Triple(
                continuity > 1,
                newAttendance.toComplete(isToday = isFirstAttendanceToday),
                isFirstAttendanceToday,
            )
        } else {
            return Triple(
                attendance.continuity > 1,
                attendance.toComplete(isToday = true),
                true,
            )
        }
    }
}
