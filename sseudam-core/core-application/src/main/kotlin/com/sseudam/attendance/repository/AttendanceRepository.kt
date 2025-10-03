package com.sseudam.attendance.repository

import com.sseudam.attendance.Attendance

interface AttendanceRepository {
    fun save(createAttendance: Attendance.Create): Attendance.Info

    fun findLastByUserId(userId: Long): Attendance.Info?
}
