package com.sseudam.batch.notification

import com.sseudam.notification.NotificationService
import com.sseudam.notification.NotificationType
import com.sseudam.notification.command.CreateNotificationStoredCommand
import com.sseudam.notification.command.FirebaseCloudMessageCommand
import com.sseudam.notification.component.NotificationStoredKeyGenerator
import com.sseudam.notification.dto.NotificationMessages
import com.sseudam.notification.fcm.FcmSender
import com.sseudam.user.UserDeviceService
import com.sseudam.user.UserService
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component

@Component
class NotificationScheduler(
    private val userDeviceService: UserDeviceService,
    private val userService: UserService,
    private val notificationStoredKeyGenerator: NotificationStoredKeyGenerator,
    private val notificationService: NotificationService,
    private val fcmSender: FcmSender,
) {
    // TODO: 테스트 후 매주 화요일 9시로 변경
    @Scheduled(cron = "0 0 9 * * *")
    fun sendWeeklyNotification() {
        val userDevices =
            userDeviceService
                .findAll()
                .sortedByDescending { it.createdAt }
                .filter { it.fcmToken.isNotBlank() && it.fcmToken.isNotEmpty() }
                .distinctBy { it.userId }

        if (userDevices.isEmpty()) return

        val userIds = userDevices.map { it.userId }.distinct()
        val users = userService.findAllBy(userIds)
        val (title, bodySuffix) = NotificationMessages.randomRegularMessage()

        val userDevicesMap = userDevices.associateBy { it.userId }
        val messages =
            users.map {
                FirebaseCloudMessageCommand(
                    fcmToken = userDevicesMap[it.id]?.fcmToken.orEmpty(),
                    title = title,
                    body = "${it.nickname}$bodySuffix",
                    destination = "HomeView",
                )
            }

        fcmSender.sendAll(messages.toSet())

        notificationService.appendAll(
            messages
                .map { message ->
                    CreateNotificationStoredCommand(
                        userId =
                            userDevicesMap.entries
                                .find { it.value.fcmToken == message.fcmToken }
                                ?.value
                                ?.userId
                                ?: return@map null,
                        notificationStoredKey = notificationStoredKeyGenerator.generate(),
                        type = NotificationType.REGULAR,
                        parameterValue = "",
                        topic = message.title,
                        contents = message.body,
                    )
                }.filterNotNull(),
        )
    }
}
