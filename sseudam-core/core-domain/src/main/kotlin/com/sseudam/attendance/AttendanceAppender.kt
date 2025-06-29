package com.sseudam.attendance

import org.springframework.stereotype.Component

@Component
class AttendanceAppender(
    private val attendanceRepository: AttendanceRepository,
) {
    fun append(create: Attendance.Create): Attendance.Info =
        attendanceRepository.save(
            create,
        )
}
