package com.sseudam.batch.pet

import com.sseudam.notification.NotificationType
import com.sseudam.notification.dto.SendNotificationMessage
import com.sseudam.notification.fcm.FcmSender
import com.sseudam.pet.UserPetFacade
import com.sseudam.user.UserDeviceService
import com.sseudam.user.UserService
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import java.time.LocalDate
import java.time.Month

@Component
class PetScheduler(
    private val userPetFacade: UserPetFacade,
    private val userDeviceService: UserDeviceService,
    private val userService: UserService,
    private val fcmSender: FcmSender,
) {
    @Scheduled(cron = "0 0 0 1 * *")
    fun createPetSeason() {
        val nextDay = LocalDate.now().plusDays(1)
        val currentYear = nextDay.year
        val currentMonth = Month.from(nextDay)
        userPetFacade.createBatchUserPet(currentYear, currentMonth)
    }

    @Scheduled(cron = "0 0 8 1 * *")
    fun createPetSeasonNotification() {
        val allDevices =
            userDeviceService
                .findAll()
                .sortedByDescending { it.createdAt }
                .filter { it.fcmToken.isNotBlank() }
                .distinctBy { it.userId }

        if (allDevices.isEmpty()) return

        val userProfiles =
            userService
                .findAllBy(allDevices.map { it.userId }.distinct())
                .associateBy { it.id }

        allDevices.forEach { device ->
            fcmSender.send(
                sendNotificationMessage =
                    SendNotificationMessage(
                        userId = device.userId,
                        title = "새로운 펫이 도착했습니다!",
                        body = "${userProfiles[device.userId]?.nickname ?: "사용자"}님의 새로운 펫을 확인하세요!",
                        destination = "MyPetView",
                    ),
                type = NotificationType.NEW_PET_SEASON,
                parameterValue = "",
                fcmToken = device.fcmToken,
            )
        }
    }
}
