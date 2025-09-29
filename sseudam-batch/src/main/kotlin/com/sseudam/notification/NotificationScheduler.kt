package com.sseudam.notification

import com.sseudam.notification.fcm.FcmSender
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component

@Component
class NotificationScheduler(
    private val notificationFacade: NotificationFacade,
    private val fcmSender: FcmSender,
) {
    // TODO: 테스트 후 매주 화요일 9시로 변경
    @Scheduled(cron = "0 0 9 * * *")
    fun sendWeeklyNotification() {
        val messages = notificationFacade.createWeeklyNotificationMessages()
        if (messages.isEmpty()) return
        fcmSender.sendAll(messages.toSet())
    }
}
