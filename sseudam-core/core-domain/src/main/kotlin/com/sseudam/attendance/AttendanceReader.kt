package com.sseudam.attendance

import org.springframework.stereotype.Component

@Component
class AttendanceReader(
    private val attendanceRepository: AttendanceRepository,
) {
    fun readLastByUser(userId: Long): Attendance.Info? = attendanceRepository.findLastByUserId(userId)
}
