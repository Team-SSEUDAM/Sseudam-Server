package com.sseudam.trashspot

import com.fasterxml.jackson.core.type.TypeReference
import com.sseudam.suggestion.SuggestionService
import com.sseudam.support.Cache
import com.sseudam.support.geo.Region
import com.sseudam.trashspot.image.TrashSpotImageService
import com.sseudam.user.UserService
import com.sseudam.visit.SpotVisitedService
import org.springframework.stereotype.Service

@Service
class TrashSpotFacade(
    private val trashSpotService: TrashSpotService,
    private val trashSpotImageService: TrashSpotImageService,
    private val suggestionService: SuggestionService,
    private val userService: UserService,
    private val visitedService: SpotVisitedService,
) {
    fun findAll(
        region: Region?,
        trashType: TrashType?,
        location: TrashSpotLocation,
    ): List<TrashSpot.Info> {
        val trashSpots = trashSpotService.findAll(region, trashType, location)
        return trashSpots
    }

    fun findDetails(spotId: Long): TrashSpotDetail =
        Cache.cache(
            ttl = 10L,
            key = "spot:detail:$spotId",
            typeReference = object : TypeReference<TrashSpotDetail>() {},
        ) {
            val spot = trashSpotService.findBy(spotId)
            val image = trashSpotImageService.findBySpotId(spotId).lastOrNull()
            val suggestioner = suggestionService.findSpotSuggestionBySite(spot.address.site)
            val user = suggestioner?.let { userService.getProfile(it.userId) }
            val visitedCount = visitedService.countBySpotId(spotId)
            return@cache TrashSpotDetail(spot, image, user, visitedCount)
        }
}
