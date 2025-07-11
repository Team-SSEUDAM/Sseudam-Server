package com.sseudam.attendance

import com.sseudam.pet.PetPointAction
import com.sseudam.pet.event.PetEventPublisher
import org.springframework.stereotype.Service

@Service
class AttendanceFacade(
    private val attendanceService: AttendanceService,
    private val petEventPublisher: PetEventPublisher,
) {
    fun todayAttendance(userId: Long): Pair<Boolean, Attendance.Complete> {
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
        return Pair(isContinuity, complete)
    }
}
