package com.sseudam.attendance

import com.sseudam.pet.PetPointAction
import com.sseudam.pet.event.UserPetContextEvent
import com.sseudam.support.tx.Tx
import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Service

@Service
class AttendanceFacade(
    private val attendanceService: AttendanceService,
    private val applicationEventPublisher: ApplicationEventPublisher,
) {
    fun todayAttendance(userId: Long): AttendanceResult =
        Tx.writeable {
            val (isContinuity, complete, isFirstAttendanceToday) = attendanceService.attendance(userId)
            // 오늘 처음 출석한 경우에만 포인트 지급
            if (!isFirstAttendanceToday) {
                val action =
                    if (complete.continuity == 5) {
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
            return@writeable AttendanceResult.of(complete, isContinuity, isFirstAttendanceToday)
        }
}
