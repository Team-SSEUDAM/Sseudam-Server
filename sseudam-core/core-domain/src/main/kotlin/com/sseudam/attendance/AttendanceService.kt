package com.sseudam.attendance

import org.springframework.stereotype.Service
import java.time.LocalDate

@Service
class AttendanceService(
    private val attendanceAppender: AttendanceAppender,
    private val attendanceReader: AttendanceReader,
) {
    fun attendance(userId: Long): Pair<Boolean, Attendance.Complete> {
        val attendance =
            attendanceReader.readByUser(userId)
                ?: attendanceAppender.append(
                    Attendance.Create(
                        userId = userId,
                        date = LocalDate.now(),
                        continuity = 1,
                    ),
                )

        val attendanceDate = attendance.date
        val currentDate = LocalDate.now()

        var isContinuity =
            attendanceDate.plusDays(1) == currentDate ||
                attendance.continuity > 1
        return if (attendanceDate == currentDate) {
            Pair(
                isContinuity,
                Attendance.Complete(
                    id = attendance.id,
                    userId = attendance.userId,
                    date = attendance.date,
                    continuity = attendance.continuity,
                    isToday = true,
                    createdAt = attendance.createdAt,
                ),
            )
        } else {
            if (attendance.continuity > 5) {
                val newAttendance =
                    attendanceAppender.append(
                        Attendance.Create(
                            userId = userId,
                            date = LocalDate.now(),
                            continuity = 1,
                        ),
                    )
                Pair(
                    false,
                    Attendance.Complete(
                        id = newAttendance.id,
                        userId = newAttendance.userId,
                        date = newAttendance.date,
                        continuity = newAttendance.continuity,
                        isToday = false,
                        createdAt = newAttendance.createdAt,
                    ),
                )
            } else {
                var additionalContinuity = attendance.continuity
                if (attendanceDate.plusDays(1) == currentDate) {
                    additionalContinuity += 1
                    isContinuity = true
                } else {
                    additionalContinuity = 1
                    isContinuity = false
                }

                val append =
                    attendanceAppender.append(
                        Attendance.Create(
                            userId = userId,
                            date = currentDate,
                            continuity = additionalContinuity,
                        ),
                    )
                Pair(
                    isContinuity,
                    Attendance.Complete(
                        id = append.id,
                        userId = append.userId,
                        date = append.date,
                        continuity = append.continuity,
                        isToday = false,
                        createdAt = append.createdAt,
                    ),
                )
            }
        }
    }
}
