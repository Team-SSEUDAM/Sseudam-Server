package com.sseudam.notification

import com.sseudam.notification.command.CreateNotificationStoredCommand
import com.sseudam.notification.command.FirebaseCloudMessageCommand
import com.sseudam.notification.component.NotificationStoredKeyGenerator
import com.sseudam.notification.dto.NotificationMessages
import com.sseudam.notification.fcm.FcmSender
import com.sseudam.support.cursor.Cursor
import com.sseudam.support.cursor.CursorRequest
import com.sseudam.user.UserDeviceService
import com.sseudam.user.UserService
import org.springframework.stereotype.Service

@Service
class NotificationFacade(
    private val userDeviceService: UserDeviceService,
    private val userService: UserService,
    private val notificationStoredKeyGenerator: NotificationStoredKeyGenerator,
    private val notificationService: NotificationService,
    private val fcmSender: FcmSender,
) {
    companion object {
        private const val DEFAULT_USER_NICKNAME = "사용자"
    }

    fun createWeeklyNotificationMessages(): List<FirebaseCloudMessageCommand> {
        val userDevices =
            userDeviceService
                .findAll()
                .sortedByDescending { it.createdAt }
                .filter { it.fcmToken.isNotBlank() && it.fcmToken.isNotEmpty() }
                .distinctBy { it.userId }
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
                        type = NotificationType.REGULAR,
                        parameterValue = "",
                        topic = message.title,
                        contents = message.body,
                    )
                }.filterNotNull(),
        )

        return messages
    }

    fun sendNewPetNotifications() {
        val userDevices =
            userDeviceService
                .findAll()
                .sortedByDescending { it.createdAt }
                .filter { it.fcmToken.isNotBlank() && it.fcmToken.isNotEmpty() }
                .distinctBy { it.userId }
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
                        CreateNotificationStoredCommand(
                            userId =
                                userDevices
                                    .find { it.fcmToken == message.fcmToken }
                                    ?.userId
                                    ?: return@map null,
                            notificationStoredKey = notificationStoredKeyGenerator.generate(),
                            type = NotificationType.NEW_PET_SEASON,
                            parameterValue = "",
                            topic = message.title,
                            contents = message.body,
                        )
                    }.filterNotNull(),
            )
        }
    }

    fun getNotifications(
        userId: Long,
        cursorRequest: CursorRequest,
    ): Cursor<NotificationStored.Info> {
        val notifications = notificationService.findAllNotifications(userId, cursorRequest)
        if (notifications.content.isEmpty()) return Cursor(listOf(), null, cursorRequest.size)
        return notifications
    }
}
