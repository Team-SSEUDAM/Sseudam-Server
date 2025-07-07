package com.sseudam.pet

import com.sseudam.notification.FcmSender
import com.sseudam.notification.NewFirebaseCloudMessage
import com.sseudam.notification.NotificationMessages
import com.sseudam.user.UserService
import com.sseudam.user.device.UserDeviceService
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import java.time.LocalDate
import java.time.Month

@Component
class PetScheduler(
    private val petService: PetService,
    private val userDeviceService: UserDeviceService,
    private val fcmSender: FcmSender,
    private val userService: UserService,
) {
    @Scheduled(cron = "0 59 23 L * *")
    fun createPetSeason() {
        val currentYear = LocalDate.now().plusDays(1).year
        val currentMonth = Month.from(LocalDate.now().plusDays(1))
        petService
            .createPetSeason(currentYear, currentMonth)
            .apply {
                val userDevices = userDeviceService.findAll().filter { it.fcmToken.isNotBlank() }
                if (userDevices.isEmpty()) return

                val userProfiles =
                    userService
                        .findAllBy(userDevices.map { it.userId }.distinct())
                        .associateBy { it.id }
                val messages =
                    userDevices.map { device ->
                        NewFirebaseCloudMessage(
                            fcmToken = device.fcmToken,
                            title = NotificationMessages.DEFAULT_TITLE,
                            body = NotificationMessages.newPetContents(userProfiles[device.userId]?.nickname ?: "사용자"),
                        )
                    }
                fcmSender.sendAll(messages.toSet())
            }
    }
}
