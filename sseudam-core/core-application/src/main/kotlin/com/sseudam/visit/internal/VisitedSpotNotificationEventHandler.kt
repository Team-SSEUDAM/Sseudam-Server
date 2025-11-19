package com.sseudam.visit.internal

import com.sseudam.notification.NotificationType
import com.sseudam.notification.dto.SendNotificationMessage
import com.sseudam.notification.fcm.FcmSender
import com.sseudam.visit.event.VisitedSpotNotificationRequestedEvent
import org.springframework.modulith.events.ApplicationModuleListener
import org.springframework.stereotype.Component

@Component
internal class VisitedSpotNotificationEventHandler(
    private val fcmSender: FcmSender,
) {
    @ApplicationModuleListener
    fun handleVisitedSpotNotification(event: VisitedSpotNotificationRequestedEvent) {
        val suggesterId = event.suggesterId ?: return
        val fcmToken = event.fcmToken
        if (fcmToken.isNullOrBlank()) return

        fcmSender.send(
            sendNotificationMessage =
                SendNotificationMessage(
                    userId = suggesterId,
                    title = event.title,
                    body = event.body,
                    destination = event.destination,
                ),
            type = NotificationType.valueOf(event.notificationType),
            parameterValue = event.parameterValue,
            fcmToken = fcmToken,
        )
    }
}
