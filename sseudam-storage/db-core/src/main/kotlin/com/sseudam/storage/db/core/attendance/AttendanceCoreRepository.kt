package com.sseudam.storage.db.core.attendance

import com.sseudam.attendance.Attendance
import com.sseudam.attendance.AttendanceRepository
import com.sseudam.support.tx.TxAdvice
import org.springframework.stereotype.Repository

@Repository
class AttendanceCoreRepository(
    private val attendanceJpaRepository: AttendanceJpaRepository,
    private val txAdvice: TxAdvice,
) : AttendanceRepository {
    override fun save(createAttendance: Attendance.Create): Attendance.Info =
        txAdvice.write {
            attendanceJpaRepository
                .save(
                    AttendanceEntity(
                        createAttendance,
                    ),
                ).toAttendanceInfo()
        }

    override fun findByUserId(userId: Long): Attendance.Info? =
        txAdvice.readOnly {
            attendanceJpaRepository
                .findFirstByUserIdOrderByDateDesc(userId)
                ?.toAttendanceInfo()
        }
}
