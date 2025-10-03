package com.sseudam.visit

import com.sseudam.common.GeoConverter
import com.sseudam.common.GeoJson
import com.sseudam.notification.dto.NotificationMessages
import com.sseudam.notification.dto.SendNotificationMessage
import com.sseudam.notification.fcm.FcmSender
import com.sseudam.pet.PetPointAction
import com.sseudam.pet.event.UserPetContextEvent
import com.sseudam.suggestion.SuggestionService
import com.sseudam.support.error.ErrorException
import com.sseudam.support.error.ErrorType
import com.sseudam.support.extension.logger
import com.sseudam.support.tx.Tx
import com.sseudam.trashspot.TrashSpot
import com.sseudam.trashspot.TrashSpotService
import com.sseudam.user.UserService
import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Service
import java.time.LocalDate
import java.time.LocalDateTime

@Service
class SpotVisitedFacade(
    private val spotVisitedService: SpotVisitedService,
    private val trashSpotService: TrashSpotService,
    private val suggestionService: SuggestionService,
    private val userService: UserService,
    private val fcmSender: FcmSender,
    private val geoConverter: GeoConverter,
    private val applicationEventPublisher: ApplicationEventPublisher,
) {
    companion object {
        private val log by logger()
    }

    fun visitSpot(
        userId: Long,
        spotId: Long,
    ): Pair<Boolean, SpotVisited.Info> {
        val result =
            Tx.writeable {
                val todayVisits = spotVisitedService.findTodaySpotVisitedByUser(userId)
                val todayVisitedSpot = todayVisits.find { it.spotId == spotId }

                if (todayVisitedSpot != null) {
                    val lastVisitTime = todayVisits.maxBy { it.visitedAt }.visitedAt
                    if (lastVisitTime.isAfter(LocalDateTime.now().minusMinutes(5))) {
                        throw ErrorException(ErrorType.SPOT_VISITED_ALREADY)
                    }
                    if (todayVisits.size >= 5) {
                        throw ErrorException(ErrorType.SPOT_VISITED_LIMIT_EXCEEDED)
                    }
                }

                val isToday = todayVisitedSpot == null && todayVisits.isEmpty()
                val spot = trashSpotService.findBy(spotId)
                val visited = spotVisitedService.append(SpotVisited.Create(userId, spotId, LocalDate.now()))

                val action = if (isToday) PetPointAction.TODAY_FIRST_SPOT_VISITED else PetPointAction.SPOT_VISITED
                applicationEventPublisher.publishEvent(
                    UserPetContextEvent(
                        userId = userId,
                        petPointAction = action,
                    ),
                )

                Triple(isToday, visited, spot)
            }

        Tx.requiresNew {
            sendVisitNotificationAsync(result.third)
        }

        return Pair(result.first, result.second)
    }

    private fun sendVisitNotificationAsync(spot: TrashSpot.Info) {
        try {
            val suggestion =
                suggestionService.findSpotSuggestionByPoint(geoConverter.geoJsonPointToJtsPoint(spot.point as GeoJson.Point)) ?: return
            val profile = userService.getProfile(suggestion.userId) ?: return
            fcmSender.send(
                sendNotificationMessage =
                    SendNotificationMessage(
                        userId = suggestion.userId,
                        title = NotificationMessages.DEFAULT_TITLE,
                        body = NotificationMessages.anonymousVisitedSpotContents(profile.nickname),
                    ),
                type = "SPOT_VISITED",
                parameterValue = spot.id.toString(),
            )
        } catch (e: Exception) {
            log.warn(e) { "Failed to send visit notification" }
        }
    }

    fun findSpotVisitedByUserId(userId: Long): List<SpotVisited.Info> {
        val spotVisited = spotVisitedService.findAllByUser(userId)
        if (spotVisited.isEmpty()) return emptyList()

        val spotIds = spotVisited.map { it.spotId }
        val spots = trashSpotService.findAllByIds(spotIds)
        val spotsMap = spots.associateBy { it.id }

        return spotVisited
            .mapNotNull { visited ->
                spotsMap[visited.spotId]?.let { spot ->
                    visited.copy(site = spot.address.site)
                }
            }.sortedByDescending { it.visitedAt }
    }
}
