package com.sseudam.attendance

import com.sseudam.pet.PetPointAction
import com.sseudam.pet.PetPointHistoryService
import com.sseudam.pet.UserPetService
import com.sseudam.pet.event.PetEventPublisher
import com.sseudam.support.tx.TxAdvice
import org.springframework.stereotype.Service

@Service
class AttendanceFacade(
    private val attendanceService: AttendanceService,
    private val userPetService: UserPetService,
    private val petPointHistoryService: PetPointHistoryService,
    private val petEventPublisher: PetEventPublisher,
    private val txAdvice: TxAdvice,
) {
    fun todayAttendance(userId: Long): Pair<Boolean, Attendance.Complete> =
        txAdvice.write {
            val attendance = attendanceService.attendance(userId)

            if (!attendance.second.isToday) {
                if (attendance.second.continuity == 5) {
                    val bonusAction = PetPointAction.BONUS_ATTENDANCE
                    val userPet = userPetService.findByUser(userId)
                    userPetService.updatePoint(userPet!!, bonusAction)
                    petPointHistoryService.append(userPet, bonusAction)
                }

                petEventPublisher.publish(userId, PetPointAction.ATTENDANCE)
            }

            return@write attendance
        }
}
