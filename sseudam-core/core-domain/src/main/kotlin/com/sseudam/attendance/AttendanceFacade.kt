package com.sseudam.attendance

import com.sseudam.pet.PetPointAction
import com.sseudam.pet.event.PetEventPublisher
import com.sseudam.support.tx.TxAdvice
import org.springframework.stereotype.Service

@Service
class AttendanceFacade(
    private val attendanceService: AttendanceService,
    private val petEventPublisher: PetEventPublisher,
    private val txAdvice: TxAdvice,
) {
    fun todayAttendance(userId: Long): Pair<Boolean, Attendance.Complete> =
        txAdvice.write {
            val (isContinuity, complete, isFirstAttendanceToday) = attendanceService.attendance(userId)

            // 오늘 처음 출석한 경우에만 포인트 지급
            if (isFirstAttendanceToday) {
                val action =
                    if (complete.continuity == 5) {
                        PetPointAction.CONTINUITY_ATTENDANCE
                    } else {
                        PetPointAction.ATTENDANCE
                    }
                petEventPublisher.publish(userId, action)
            }
            return@write Pair(isContinuity, complete)
        }
}
