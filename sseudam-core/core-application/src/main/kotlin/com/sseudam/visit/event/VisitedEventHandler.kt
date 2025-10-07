package com.sseudam.visit.event

import com.sseudam.common.GeoConverter
import com.sseudam.common.GeoJson
import com.sseudam.notification.dto.NotificationMessages
import com.sseudam.notification.dto.SendNotificationMessage
import com.sseudam.notification.fcm.FcmSender
import com.sseudam.pet.event.UserPetContextEvent
import com.sseudam.suggestion.SuggestionService
import com.sseudam.support.extension.logger
import com.sseudam.user.UserService
import org.springframework.context.ApplicationEventPublisher
import org.springframework.modulith.events.ApplicationModuleListener
import org.springframework.stereotype.Component

@Component
class VisitedEventHandler(
    private val suggestionService: SuggestionService,
    private val userService: UserService,
    private val fcmSender: FcmSender,
    private val geoConverter: GeoConverter,
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
            val suggestion =
                suggestionService.findSpotSuggestionByPoint(geoConverter.geoJsonPointToJtsPoint(event.spot.point as GeoJson.Point))
                    ?: return
            val profile = userService.getProfile(suggestion.userId) ?: return
            fcmSender.send(
                sendNotificationMessage =
                    SendNotificationMessage(
                        userId = suggestion.userId,
                        title = NotificationMessages.DEFAULT_TITLE,
                        body = NotificationMessages.anonymousVisitedSpotContents(profile.nickname),
                    ),
                type = "SPOT_VISITED",
                parameterValue = event.spot.id.toString(),
            )
        } catch (e: Exception) {
            log.warn(e) { "Failed to send visit notification" }
        }
    }
}
