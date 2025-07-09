package com.sseudam.visit

import com.sseudam.notification.FcmSender
import com.sseudam.notification.NotificationMessages
import com.sseudam.notification.SendNotificationMessage
import com.sseudam.pet.PetPointAction
import com.sseudam.pet.event.PetEventPublisher
import com.sseudam.suggestion.SuggestionService
import com.sseudam.support.error.ErrorException
import com.sseudam.support.error.ErrorType
import com.sseudam.support.extension.logger
import com.sseudam.trashspot.TrashSpot
import com.sseudam.trashspot.TrashSpotService
import com.sseudam.user.UserService
import org.springframework.stereotype.Service
import java.time.LocalDate
import java.time.LocalDateTime

@Service
class SpotVisitedFacade(
    private val spotVisitedService: SpotVisitedService,
    private val trashSpotService: TrashSpotService,
    private val petEventPublisher: PetEventPublisher,
    private val suggestionService: SuggestionService,
    private val userService: UserService,
    private val fcmSender: FcmSender,
) {
    companion object {
        private val log by logger()
    }

    fun visitSpot(
        userId: Long,
        spotId: Long,
    ): SpotVisited.Info {
        val todayVisits = spotVisitedService.findTodaySpotVisitedByUser(userId, spotId)
        if (todayVisits.isNotEmpty()) {
            if (todayVisits.maxBy { it.visitedAt }.visitedAt <= LocalDateTime.now().plusMinutes(5)) {
                throw ErrorException(ErrorType.SPOT_VISITED_ALREADY)
            }
            if (todayVisits.size >= 5) {
                throw ErrorException(ErrorType.SPOT_VISITED_LIMIT_EXCEEDED)
            }
        }

        val spot = trashSpotService.findBy(spotId)
        val visited = spotVisitedService.append(SpotVisited.Create(userId, spotId, LocalDate.now()))

        petEventPublisher.publish(visited.userId, PetPointAction.SPOT_VISITED)

        sendVisitNotificationAsync(spot)
        return visited
    }

    private fun sendVisitNotificationAsync(spot: TrashSpot.Info) {
        try {
            val suggestion = suggestionService.findSpotSuggestionBySite(spot.address.site) ?: return
            val profile = userService.getProfile(suggestion.userId)
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
            log.warn("Failed to send visit notification", e)
        }
    }

    fun findSpotVisitedByUserId(userId: Long): SpotVisitedAll {
        val spotVisited = spotVisitedService.findAllByUser(userId)
        if (spotVisited.isEmpty()) return SpotVisitedAll(emptyList())

        val spotIds = spotVisited.map { it.spotId }
        val spots = trashSpotService.findAllByIds(spotIds)
        return SpotVisitedAll.of(spotVisited, spots)
    }
}
