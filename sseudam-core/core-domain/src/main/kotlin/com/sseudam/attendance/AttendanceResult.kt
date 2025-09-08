package com.sseudam.attendance

import java.time.LocalDate
import java.time.LocalDateTime

data class AttendanceResult(
    val userId: Long,
    val date: LocalDate,
    val isToday: Boolean,
    val continuity: Int,
    val isContinuity: Boolean,
    val createdAt: LocalDateTime,
) {
    companion object {
        fun of(
            complete: Attendance.Complete,
            isContinuity: Boolean,
            isFirstAttendanceToday: Boolean,
        ): AttendanceResult =
            AttendanceResult(
                userId = complete.userId,
                date = complete.date,
                isToday = isFirstAttendanceToday,
                continuity = complete.continuity,
                isContinuity = isContinuity,
                createdAt = complete.createdAt,
            )
    }
}
