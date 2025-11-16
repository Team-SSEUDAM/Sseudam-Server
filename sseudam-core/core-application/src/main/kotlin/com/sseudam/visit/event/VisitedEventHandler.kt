package com.sseudam.visit.event

import com.sseudam.notification.NotificationType
import com.sseudam.notification.dto.NotificationMessages
import com.sseudam.pet.event.UserPetContextEvent
import com.sseudam.support.extension.logger
import com.sseudam.user.UserDeviceService
import com.sseudam.user.UserService
import org.springframework.context.ApplicationEventPublisher
import org.springframework.modulith.events.ApplicationModuleListener
import org.springframework.stereotype.Component

@Component
class VisitedEventHandler(
    private val userService: UserService,
    private val userDeviceService: UserDeviceService,
    private val applicationEventPublisher: ApplicationEventPublisher,
) {
    companion object {
        private val log by logger()
    }

    @ApplicationModuleListener(
        id = "handle-visited-pet-context-event",
    )
    fun handlePetContextEvent(event: SpotVisitedEvent) {
        applicationEventPublisher.publishEvent(
            UserPetContextEvent(
                userId = event.userId,
                petPointAction = event.petPointAction,
            ),
        )
    }

    @ApplicationModuleListener(
        id = "send-visit-notification",
    )
    fun handleSendVisitNotification(event: SpotVisitedEvent) {
        try {
            val suggesterId = event.suggesterId ?: return
            val profile = userService.getProfile(suggesterId) ?: return
            val userDevices = userDeviceService.findAllByUserId(suggesterId)
            val fcmToken = userDevices.lastOrNull()?.fcmToken ?: return

            applicationEventPublisher.publishEvent(
                VisitedSpotNotificationRequestedEvent(
                    suggesterId = suggesterId,
                    title = NotificationMessages.DEFAULT_TITLE,
                    body = NotificationMessages.anonymousVisitedSpotContents(profile.nickname),
                    destination = "MyPageView",
                    notificationType = NotificationType.ANONYMOUS_VISITED_SPOT.name,
                    parameterValue = event.spotId.toString(),
                    fcmToken = fcmToken,
                ),
            )
        } catch (e: Exception) {
            log.warn(e) { "Failed to send visit notification" }
        }
    }
}
