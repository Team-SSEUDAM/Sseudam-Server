package com.sseudam.attendance

import com.sseudam.attendance.result.AttendanceResult
import com.sseudam.pet.PetPointAction
import com.sseudam.pet.event.UserPetContextEvent
import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class AttendanceFacade(
    private val attendanceService: AttendanceService,
    private val applicationEventPublisher: ApplicationEventPublisher,
) {
    @Transactional
    fun todayAttendance(userId: Long): AttendanceResult {
        val attendanceResult = attendanceService.attendance(userId)
        // 오늘 처음 출석한 경우에만 포인트 지급
        if (!attendanceResult.isToday) {
            val action =
                if (attendanceResult.continuity == 5) {
                    PetPointAction.CONTINUITY_ATTENDANCE
                } else {
                    PetPointAction.ATTENDANCE
                }
            applicationEventPublisher.publishEvent(
                UserPetContextEvent(
                    userId = userId,
                    petPointAction = action,
                ),
            )
        }
        return attendanceResult
    }
}
