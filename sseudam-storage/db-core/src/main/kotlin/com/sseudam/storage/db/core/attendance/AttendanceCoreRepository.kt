package com.sseudam.storage.db.core.attendance

import com.sseudam.attendance.Attendance
import com.sseudam.attendance.AttendanceRepository
import com.sseudam.support.tx.Tx
import org.springframework.stereotype.Repository

@Repository
class AttendanceCoreRepository(
    private val attendanceJpaRepository: AttendanceJpaRepository,
) : AttendanceRepository {
    override fun save(createAttendance: Attendance.Create): Attendance.Info =
        Tx.writeable {
            attendanceJpaRepository
                .save(
                    AttendanceEntity(
                        createAttendance,
                    ),
                ).toAttendanceInfo()
        }

    override fun findLastByUserId(userId: Long): Attendance.Info? =
        Tx.readable {
            attendanceJpaRepository
                .findFirstByUserIdOrderByDateDesc(userId)
                ?.toAttendanceInfo()
        }
}
