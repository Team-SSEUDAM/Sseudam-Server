package com.sseudam.attendance

import org.springframework.stereotype.Component

@Component
class AttendanceReader(
    private val attendanceRepository: AttendanceRepository,
) {
    fun readByUser(userId: Long): Attendance.Info? = attendanceRepository.findByUserId(userId)
}
