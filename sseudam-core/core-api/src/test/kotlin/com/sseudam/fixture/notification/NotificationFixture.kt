package com.sseudam.fixture.notification

import com.navercorp.fixturemonkey.kotlin.setExp
import com.sseudam.notification.NotificationStored
import com.sseudam.notification.NotificationType
import com.sseudam.notification.ReadStatus
import com.sseudam.notification.command.CreateNotificationStoredCommand
import com.sseudam.support.cursor.Cursor
import com.sseudam.test.helper.fixtureBuilder
import com.sseudam.user.device.UserDevice
import java.time.LocalDateTime

object NotificationFixture {
    val notificationStoredInfo =
        fixtureBuilder<NotificationStored.Info> {
            setExp(NotificationStored.Info::id, 1L)
            setExp(NotificationStored.Info::notificationStoredKey, "notification-key-1")
            setExp(NotificationStored.Info::userId, 1L)
            setExp(NotificationStored.Info::type, NotificationType.APPROVE_SUGGESTION)
            setExp(NotificationStored.Info::parameterValue, "1")
            setExp(NotificationStored.Info::topic, "쓰담")
            setExp(NotificationStored.Info::contents, "누군가 쓰담님이 제보한 쓰레기통에 쓰레기를 버렸어요! 지금 확인해보러 갈까요?")
            setExp(NotificationStored.Info::readStatus, ReadStatus.UNREAD)
            setExp(NotificationStored.Info::createdAt, "2025-10-08T17:15:28.001012")
        }

    val notificationStoredCreate =
        fixtureBuilder<NotificationStored.Create> {
            setExp(NotificationStored.Create::notificationStoredKey, "notification-key-1")
            setExp(NotificationStored.Create::userId, 1L)
            setExp(NotificationStored.Create::type, NotificationType.APPROVE_SUGGESTION)
            setExp(NotificationStored.Create::parameterValue, "1")
            setExp(NotificationStored.Create::topic, "쓰담")
            setExp(NotificationStored.Create::contents, "누군가 쓰담님이 제보한 쓰레기통에 쓰레기를 버렸어요!")
            setExp(NotificationStored.Create::readStatus, ReadStatus.UNREAD)
        }

    val createNotificationStoredCommand =
        fixtureBuilder<CreateNotificationStoredCommand> {
            setExp(CreateNotificationStoredCommand::userId, 1L)
            setExp(CreateNotificationStoredCommand::notificationStoredKey, "notification-key-1")
            setExp(CreateNotificationStoredCommand::type, NotificationType.APPROVE_SUGGESTION)
            setExp(CreateNotificationStoredCommand::parameterValue, "1")
            setExp(CreateNotificationStoredCommand::topic, "쓰담")
            setExp(CreateNotificationStoredCommand::contents, "누군가 쓰담님이 제보한 쓰레기통에 쓰레기를 버렸어요!")
        }

    val notificationCursor =
        fixtureBuilder<Cursor<NotificationStored.Info>> {
            setExp(Cursor<NotificationStored.Info>::content, listOf(notificationStoredInfo))
            setExp(Cursor<NotificationStored.Info>::nextCursor, 2L)
        }

    val userDeviceInfo =
        fixtureBuilder<UserDevice.Info> {
            setExp(UserDevice.Info::id, 1L)
            setExp(UserDevice.Info::userId, 1L)
            setExp(UserDevice.Info::userKey, "user-key-1")
            setExp(UserDevice.Info::deviceId, "device-1")
            setExp(UserDevice.Info::fcmToken, "fcm-token-1")
            setExp(UserDevice.Info::createdAt, LocalDateTime.now())
        }
}
