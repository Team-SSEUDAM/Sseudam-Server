package com.sseudam.notification

import com.sseudam.user.UserService
import com.sseudam.user.device.UserDeviceService
import org.springframework.stereotype.Service

@Service
class NotificationFacade(
    private val userDeviceService: UserDeviceService,
    private val userService: UserService,
    private val fcmSender: FcmSender,
) {
    fun sendWeeklyNotification() {
        val userDevices = userDeviceService.findAll().filter { it.fcmToken.isNotBlank() }
        if (userDevices.isEmpty()) return

        val userIds = userDevices.map { it.userId }.distinct()
        val users = userService.findAllBy(userIds)
        val (title, bodySuffix) = NotificationRegularMessage.randomMessage()

        val userDevicesMap = userDevices.associateBy { it.userId }
        val messages =
            users.map {
                NewFirebaseCloudMessage(
                    fcmToken = userDevicesMap[it.id]?.fcmToken.orEmpty(),
                    title = title,
                    body = "${it.nickname}$bodySuffix",
                )
            }

        fcmSender.sendAll(messages.toSet())
    }
}
