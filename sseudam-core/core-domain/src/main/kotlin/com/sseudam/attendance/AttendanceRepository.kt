package com.sseudam.attendance

interface AttendanceRepository {
    fun save(createAttendance: Attendance.Create): Attendance.Info

    fun findLastByUserId(userId: Long): Attendance.Info?
}
