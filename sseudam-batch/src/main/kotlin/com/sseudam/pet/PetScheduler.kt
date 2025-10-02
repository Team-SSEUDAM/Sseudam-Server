package com.sseudam.pet

import com.sseudam.notification.NotificationMessages
import com.sseudam.notification.NotificationService
import com.sseudam.notification.NotificationStored
import com.sseudam.notification.ReadStatus
import com.sseudam.notification.command.FirebaseCloudMessageCommand
import com.sseudam.notification.component.NotificationStoredKeyGenerator
import com.sseudam.notification.fcm.FcmSender
import com.sseudam.user.UserDeviceService
import com.sseudam.user.UserService
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
    private val userPetService: UserPetService,
    private val notificationService: NotificationService,
    private val notificationStoredKeyGenerator: NotificationStoredKeyGenerator,
) {
    @Scheduled(cron = "0 0 0 1 * *")
    fun createPetSeason() {
        val nextDay = LocalDate.now().plusDays(1)
        val currentYear = nextDay.year
        val currentMonth = Month.from(nextDay)
        val createLevelOnePet = petService.createPetSeason(currentYear, currentMonth)
        val allUserPet = userPetService.findAll()
        userPetService.initPointForAllUsers(allUserPet, createLevelOnePet.id)
        sendNewPetNotifications()
    }

    private fun sendNewPetNotifications() {
        val userDevices =
            userDeviceService
                .findAll()
                .filter { it.fcmToken.isNotBlank() }
        if (userDevices.isEmpty()) return
        val userProfiles =
            userService
                .findAllBy(userDevices.map { it.userId }.distinct())
                .associateBy { it.id }
        val messages =
            userDevices.map { device ->
                FirebaseCloudMessageCommand(
                    fcmToken = device.fcmToken,
                    title = NotificationMessages.DEFAULT_TITLE,
                    body =
                        NotificationMessages.newPetContents(
                            userProfiles[device.userId]?.nickname
                                ?: DEFAULT_USER_NICKNAME,
                        ),
                )
            }
        fcmSender.sendAll(messages.toSet()).apply {
            notificationService.appendAll(
                messages
                    .map { message ->
                        NotificationStored.Create(
                            userId =
                                userDevices
                                    .find { it.fcmToken == message.fcmToken }
                                    ?.userId
                                    ?: return@map null,
                            notificationStoredKey = notificationStoredKeyGenerator.generate(),
                            type = "PET_SEASON",
                            parameterValue = "",
                            topic = message.title,
                            contents = message.body,
                            readStatus = ReadStatus.UNREAD,
                        )
                    }.filterNotNull(),
            )
        }
    }

    companion object {
        private const val DEFAULT_USER_NICKNAME = "사용자"
    }
}
