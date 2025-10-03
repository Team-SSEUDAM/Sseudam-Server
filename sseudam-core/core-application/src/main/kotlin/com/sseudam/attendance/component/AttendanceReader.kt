package com.sseudam.attendance.component

import com.sseudam.attendance.Attendance
import com.sseudam.attendance.repository.AttendanceRepository
import org.springframework.stereotype.Component

@Component
class AttendanceReader(
    private val attendanceRepository: AttendanceRepository,
) {
    fun readLastByUser(userId: Long): Attendance.Info? = attendanceRepository.findLastByUserId(userId)
}
