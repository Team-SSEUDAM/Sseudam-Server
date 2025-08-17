package com.sseudam.storage.db.core.attendance

import com.sseudam.attendance.Attendance
import com.sseudam.storage.db.core.support.BaseEntity
import jakarta.persistence.Entity
import jakarta.persistence.Table
import java.time.LocalDate

@Entity
@Table(name = "t_attendance")
class AttendanceEntity(
    val userId: Long,
    val date: LocalDate,
    val continuity: Int = 1,
) : BaseEntity() {
    constructor(attendanceCreate: Attendance.Create) : this(
        userId = attendanceCreate.userId,
        date = attendanceCreate.date,
        continuity = attendanceCreate.continuity,
    )

    fun toAttendanceInfo() =
        Attendance.Info(
            id = id!!,
            userId = userId,
            date = date,
            continuity = continuity,
            createdAt = createdAt,
        )
}
