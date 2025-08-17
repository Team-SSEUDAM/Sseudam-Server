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
        val attendance = attendanceReader.readByUser(userId)
        val isFirstAttendanceToday = attendance?.date != currentDate

        return if (attendance == null || isFirstAttendanceToday) {
            val continuity = if (attendance?.date?.plusDays(1) == currentDate) ((attendance?.continuity ?: 0) + 1).coerceAtMost(5) else 1
            val newAttendance =
                attendanceAppender.append(
                    Attendance.Create(userId, currentDate, continuity),
                )
            Triple(
                continuity > 1,
                Attendance.Complete(
                    id = newAttendance.id,
                    userId = newAttendance.userId,
                    date = newAttendance.date,
                    continuity = newAttendance.continuity,
                    isToday = true,
                    createdAt = newAttendance.createdAt,
                ),
                true,
            )
        } else {
            Triple(
                attendance.continuity > 1,
                Attendance.Complete(
                    id = attendance.id,
                    userId = attendance.userId,
                    date = attendance.date,
                    continuity = attendance.continuity,
                    isToday = true,
                    createdAt = attendance.createdAt,
                ),
                false,
            )
        }
    }
}
