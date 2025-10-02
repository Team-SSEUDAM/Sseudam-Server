package com.sseudam.attendance.component

import com.sseudam.attendance.Attendance
import com.sseudam.attendance.repository.AttendanceRepository
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
