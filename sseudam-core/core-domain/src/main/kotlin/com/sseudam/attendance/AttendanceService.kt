package com.sseudam.attendance

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
        println("attendacne: $attendance $currentDate")
        val isFirstAttendanceToday = attendance?.date == currentDate

        println("isFirstAttendanceToday: $isFirstAttendanceToday")

        if (attendance == null || !isFirstAttendanceToday) {
            val continuity =
                if (attendance?.date?.plusDays(1) == currentDate) {
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
