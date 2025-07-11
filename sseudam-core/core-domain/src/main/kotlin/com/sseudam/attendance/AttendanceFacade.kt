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
            val (isContinuity, complete) = attendanceService.attendance(userId)
            if (!complete.isToday) {
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
