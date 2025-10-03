package com.sseudam.notification

import com.sseudam.notification.command.CreateNotificationStoredCommand
import com.sseudam.notification.command.FirebaseCloudMessageCommand
import com.sseudam.notification.component.NotificationStoredKeyGenerator
import com.sseudam.notification.dto.NotificationMessages
import com.sseudam.user.UserDeviceService
import com.sseudam.user.UserService
import org.springframework.stereotype.Service

@Service
class NotificationFacade(
    private val userDeviceService: UserDeviceService,
    private val userService: UserService,
    private val notificationStoredKeyGenerator: NotificationStoredKeyGenerator,
    private val notificationService: NotificationService,
) {
    fun createWeeklyNotificationMessages(): List<FirebaseCloudMessageCommand> {
        val userDevices = userDeviceService.findAll().filter { it.fcmToken.isNotBlank() }
        if (userDevices.isEmpty()) {
            return listOf()
        }

        val userIds = userDevices.map { it.userId }.distinct()
        val users = userService.findAllBy(userIds)
        val (title, bodySuffix) = NotificationMessages.randomMessage()

        val userDevicesMap = userDevices.associateBy { it.userId }
        val messages =
            users.map {
                FirebaseCloudMessageCommand(
                    fcmToken = userDevicesMap[it.id]?.fcmToken.orEmpty(),
                    title = title,
                    body = "${it.nickname}$bodySuffix",
                )
            }

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
                        type = "REGULAR",
                        parameterValue = "",
                        topic = message.title,
                        contents = message.body,
                    )
                }.filterNotNull(),
        )

        return messages
    }
}
