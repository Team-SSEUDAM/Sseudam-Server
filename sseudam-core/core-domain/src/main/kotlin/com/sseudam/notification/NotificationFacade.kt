package com.sseudam.notification

import com.sseudam.user.UserService
import com.sseudam.user.device.UserDeviceService
import org.springframework.stereotype.Service

@Service
class NotificationFacade(
    private val userDeviceService: UserDeviceService,
    private val userService: UserService,
    private val notificationStoredKeyGenerator: NotificationStoredKeyGenerator,
    private val notificationService: NotificationService,
) {
    fun createWeeklyNotificationMessages(): List<NewFirebaseCloudMessage> {
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
                NewFirebaseCloudMessage(
                    fcmToken = userDevicesMap[it.id]?.fcmToken.orEmpty(),
                    title = title,
                    body = "${it.nickname}$bodySuffix",
                )
            }

        notificationService.appendAll(
            messages
                .map { message ->
                    NotificationStored.Create(
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
                        readStatus = ReadStatus.UNREAD,
                    )
                }.filterNotNull(),
        )

        return messages
    }
}
